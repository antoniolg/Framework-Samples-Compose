#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(git rev-parse --show-toplevel)"
cd "$ROOT_DIR"

REPORT_FILE="${1:-reports/history-build-audit.tsv}"
mkdir -p "$(dirname "$REPORT_FILE")"

BUILD_FILES=(
  gradle/libs.versions.toml
  app/build.gradle.kts
  build.gradle.kts
  settings.gradle.kts
  gradle/wrapper/gradle-wrapper.properties
)

VERSION_KEYS=(
  agp
  kotlin
  ksp
  coreKtx
  junitVersion
  espressoCore
  lifecycleRuntimeKtx
  activityCompose
  composeBom
  navigation
  kotlinx-serialization
  datastore
  room
  workmanager
  coil
  retrofit
  okhttp
  gson
  appcompat
  material
  constraintlayout
  fragment
  play-services-location
  play-services-maps
  maps-compose
)

# Target versions resolved on 2026-03-02.
target_for_key() {
  case "$1" in
    agp) echo "9.0.1" ;;
    kotlin) echo "2.3.10" ;;
    ksp) echo "2.3.6" ;;
    coreKtx) echo "1.17.0" ;;
    junitVersion) echo "1.3.0" ;;
    espressoCore) echo "3.7.0" ;;
    lifecycleRuntimeKtx) echo "2.10.0" ;;
    activityCompose) echo "1.12.4" ;;
    composeBom) echo "2026.02.01" ;;
    navigation) echo "2.9.7" ;;
    kotlinx-serialization) echo "1.10.0" ;;
    datastore) echo "1.2.0" ;;
    room) echo "2.8.4" ;;
    workmanager) echo "2.11.1" ;;
    coil) echo "2.7.0" ;;
    retrofit) echo "3.0.0" ;;
    okhttp) echo "5.3.2" ;;
    gson) echo "2.13.2" ;;
    appcompat) echo "1.7.1" ;;
    material) echo "1.13.0" ;;
    constraintlayout) echo "2.2.1" ;;
    fragment) echo "1.8.9" ;;
    play-services-location) echo "21.3.0" ;;
    play-services-maps) echo "20.0.0" ;;
    maps-compose) echo "8.2.0" ;;
    *) echo "" ;;
  esac
}

TARGET_WRAPPER="9.3.1"

extract_toml_value() {
  local file_content="$1"
  local key="$2"
  awk -F'=' -v key="$key" '
    $1 ~ "^[[:space:]]*" key "[[:space:]]*$" {
      val=$2
      gsub(/[[:space:]\"]/, "", val)
      print val
      exit
    }
  ' <<< "$file_content"
}

extract_wrapper_version() {
  local file_content="$1"
  sed -n 's#.*gradle-\([0-9][0-9.]*\)-bin\.zip#\1#p' <<< "$file_content" | head -n 1
}

is_version_numeric() {
  local version="$1"
  [[ "$version" =~ ^[0-9]+([.-][0-9]+)*$ ]]
}

is_version_less_than() {
  local current="$1"
  local target="$2"
  if [[ "$current" == "$target" ]]; then
    return 1
  fi
  if ! is_version_numeric "$current" || ! is_version_numeric "$target"; then
    return 1
  fi
  [[ "$(printf '%s\n%s\n' "$current" "$target" | sort -V | head -n 1)" == "$current" ]]
}

COMMITS=()
while IFS= read -r commit; do
  COMMITS+=("$commit")
done < <(git rev-list --reverse HEAD -- "${BUILD_FILES[@]}")

# Use detached worktree for optional Gradle/lint checks without polluting cwd.
TMP_WORKTREE="$(mktemp -d)"
cleanup() {
  git worktree remove -f "$TMP_WORKTREE" >/dev/null 2>&1 || true
  rm -rf "$TMP_WORKTREE"
}
trap cleanup EXIT

git worktree add --detach "$TMP_WORKTREE" HEAD >/dev/null

{
  printf 'commit\tdate\tsubject\tchanged_files\toutdated_count\tmissing_count\twrapper\twrapper_status\tgradle_help\tlint\n'

  for commit in "${COMMITS[@]}"; do
    subject="$(git show -s --format=%s "$commit")"
    date="$(git show -s --format=%cs "$commit")"
    changed_files="$(git show --name-only --pretty='' "$commit" -- "${BUILD_FILES[@]}" | tr '\n' ';' | sed 's/;$//')"

    catalog_content="$(git show "$commit:gradle/libs.versions.toml" 2>/dev/null || true)"
    wrapper_content="$(git show "$commit:gradle/wrapper/gradle-wrapper.properties" 2>/dev/null || true)"

    outdated_count=0
    missing_count=0

    for key in "${VERSION_KEYS[@]}"; do
      target="$(target_for_key "$key")"
      value=""
      if [[ -n "$catalog_content" ]]; then
        value="$(extract_toml_value "$catalog_content" "$key")"
      fi

      if [[ -z "$value" ]]; then
        missing_count=$((missing_count + 1))
      elif is_version_less_than "$value" "$target"; then
        outdated_count=$((outdated_count + 1))
      fi
    done

    wrapper_version=""
    wrapper_status="MISSING"
    if [[ -n "$wrapper_content" ]]; then
      wrapper_version="$(extract_wrapper_version "$wrapper_content")"
      if [[ -n "$wrapper_version" ]]; then
        if is_version_less_than "$wrapper_version" "$TARGET_WRAPPER"; then
          wrapper_status="OUTDATED"
        elif [[ "$wrapper_version" == "$TARGET_WRAPPER" ]]; then
          wrapper_status="OK"
        else
          wrapper_status="NEWER"
        fi
      fi
    fi

    gradle_help="SKIPPED"
    lint="SKIPPED"

    if [[ "${RUN_GRADLE_HELP:-0}" == "1" || "${RUN_LINT:-0}" == "1" ]]; then
      git -C "$TMP_WORKTREE" checkout -q "$commit"
    fi

    if [[ "${RUN_GRADLE_HELP:-0}" == "1" ]]; then
      if (cd "$TMP_WORKTREE" && ./gradlew -q help >/dev/null 2>&1); then
        gradle_help="OK"
      else
        gradle_help="FAIL"
      fi
    fi

    if [[ "${RUN_LINT:-0}" == "1" ]]; then
      if [[ -f "$TMP_WORKTREE/app/build.gradle.kts" ]]; then
        if (cd "$TMP_WORKTREE" && ./gradlew :app:lintDebug >/dev/null 2>&1); then
          lint="OK"
        else
          lint="FAIL"
        fi
      else
        lint="N/A"
      fi
    fi

    printf '%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' \
      "$commit" "$date" "$subject" "$changed_files" "$outdated_count" "$missing_count" \
      "${wrapper_version:-}" "$wrapper_status" "$gradle_help" "$lint"
  done
} > "$REPORT_FILE"

echo "Wrote audit report: $REPORT_FILE"
