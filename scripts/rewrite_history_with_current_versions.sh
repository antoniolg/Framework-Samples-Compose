#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(git rev-parse --show-toplevel)"
cd "$ROOT_DIR"

if [[ -n "$(git status --porcelain)" ]]; then
  echo "Working tree must be clean before rewriting history." >&2
  exit 1
fi

CATALOG_FILE="$ROOT_DIR/gradle/libs.versions.toml"
WRAPPER_FILE="$ROOT_DIR/gradle/wrapper/gradle-wrapper.properties"

if [[ ! -f "$CATALOG_FILE" || ! -f "$WRAPPER_FILE" ]]; then
  echo "Missing version catalog or Gradle wrapper configuration." >&2
  exit 1
fi

VERSION_KEYS=(
  agp
  kotlin
  coreKtx
  junit
  junitVersion
  espressoCore
  lifecycleRuntimeKtx
  activityCompose
  composeBom
  navigation
  kotlinx-serialization
  datastore
  room
  ksp
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
  secrets-gradle-plugin
)

get_catalog_version() {
  local key="$1"
  awk -F'=' -v key="$key" '
    $1 ~ "^[[:space:]]*" key "[[:space:]]*$" {
      val=$2
      gsub(/[[:space:]"]/, "", val)
      print val
      exit
    }
  ' "$CATALOG_FILE"
}

get_wrapper_version() {
  sed -n 's#.*gradle-\([0-9][0-9.]*\)-bin\.zip#\1#p' "$WRAPPER_FILE" | head -n 1
}

TMP_HELPER="$(mktemp)"
cleanup() {
  rm -f "$TMP_HELPER"
}
trap cleanup EXIT

{
  echo '#!/usr/bin/env bash'
  echo 'set -euo pipefail'
  echo
  echo 'if [[ -f gradle/libs.versions.toml ]]; then'

  for key in "${VERSION_KEYS[@]}"; do
    value="$(get_catalog_version "$key")"
    if [[ -n "$value" ]]; then
      printf "  perl -0pi -e 's/^%s = \".*\"/%s = \"%s\"/m' gradle/libs.versions.toml\n" "$key" "$key" "$value"
    fi
  done

  echo 'fi'
  echo
  echo 'if [[ -f gradle/wrapper/gradle-wrapper.properties ]]; then'
  printf "  perl -0pi -e 's#distributionUrl=.*gradle-[0-9.]+-bin\\\\.zip#distributionUrl=https\\\\://services.gradle.org/distributions/gradle-%s-bin.zip#' gradle/wrapper/gradle-wrapper.properties\n" "$(get_wrapper_version)"
  echo 'fi'
} > "$TMP_HELPER"

chmod +x "$TMP_HELPER"

git rebase --root --empty=drop --exec "$TMP_HELPER"
