#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(git rev-parse --show-toplevel)"
CATALOG_FILE="$ROOT_DIR/gradle/libs.versions.toml"
WRAPPER_FILE="$ROOT_DIR/gradle/wrapper/gradle-wrapper.properties"

if [[ ! -f "$CATALOG_FILE" ]]; then
  echo "Missing version catalog: $CATALOG_FILE" >&2
  exit 1
fi

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

repo_url() {
  local repo="$1"
  local group="$2"
  local artifact="$3"
  local group_path
  group_path="$(printf '%s' "$group" | tr '.' '/')"
  case "$repo" in
    google)
      echo "https://dl.google.com/dl/android/maven2/${group_path}/${artifact}/maven-metadata.xml"
      ;;
    central)
      echo "https://repo1.maven.org/maven2/${group_path}/${artifact}/maven-metadata.xml"
      ;;
    gradleportal)
      echo "https://plugins.gradle.org/m2/${group_path}/${artifact}/maven-metadata.xml"
      ;;
    gradlecurrent)
      echo "https://services.gradle.org/versions/current"
      ;;
    *)
      return 1
      ;;
  esac
}

fetch_versions() {
  local repo="$1"
  local group="$2"
  local artifact="$3"

  if [[ "$repo" == "gradlecurrent" ]]; then
    curl -fsSL "$(repo_url "$repo" "$group" "$artifact")" | sed -n 's/.*"version" *: *"\([0-9.]*\)".*/\1/p'
    return 0
  fi

  curl -fsSL "$(repo_url "$repo" "$group" "$artifact")" \
    | sed -n 's:.*<version>\(.*\)</version>.*:\1:p'
}

latest_stable_version() {
  # Accept 1.2.3 and 2.1.20-1.0.32 style versions, reject alphas/rc/beta/m
  grep -E '^[0-9]+([.-][0-9]+)*$' | sort -V | tail -n 1
}

resolve_latest() {
  local group="$1"
  local artifact="$2"
  local repos_csv="$3"

  local all_versions=""
  local source_list=""

  IFS=',' read -r -a repos <<< "$repos_csv"
  for repo in "${repos[@]}"; do
    local source
    source="$(repo_url "$repo" "$group" "$artifact")"
    source_list+="$source;"
    local versions
    versions="$(fetch_versions "$repo" "$group" "$artifact" 2>/dev/null || true)"
    if [[ -n "$versions" ]]; then
      all_versions+="$versions"$'\n'
    fi
  done

  local latest=""
  if [[ -n "$all_versions" ]]; then
    latest="$(printf "%s" "$all_versions" | latest_stable_version || true)"
  fi

  printf '%s\t%s' "${latest:-?}" "$source_list"
}

report_line() {
  local name="$1"
  local current="$2"
  local group="$3"
  local artifact="$4"
  local repos="$5"

  local resolved
  resolved="$(resolve_latest "$group" "$artifact" "$repos")"
  local latest
  latest="$(printf "%s" "$resolved" | cut -f1)"
  local sources
  sources="$(printf "%s" "$resolved" | cut -f2)"

  local status="NEEDS_MANUAL"
  if [[ "$latest" == "?" ]]; then
    status="NEEDS_MANUAL"
  elif [[ "$current" == "$latest" ]]; then
    status="OK"
  else
    status="OUTDATED"
  fi

  printf '| %s | %s | %s | %s | %s | %s |\n' "$name" "$current" "$latest" "$status" "$group:$artifact" "$sources"
}

# Header
printf '| Name | Current | Latest Stable | Status | Coordinate | Sources |\n'
printf '|---|---:|---:|---|---|---|\n'

# Plugins/toolchain
report_line "agp" "$(get_catalog_version agp)" "com.android.tools.build" "gradle" "google"
report_line "kotlin" "$(get_catalog_version kotlin)" "org.jetbrains.kotlin" "kotlin-gradle-plugin" "central,gradleportal"
report_line "ksp" "$(get_catalog_version ksp)" "com.google.devtools.ksp" "symbol-processing-gradle-plugin" "google,central,gradleportal"
report_line "androidx-navigation-safeargs" "$(get_catalog_version navigation)" "androidx.navigation" "navigation-safe-args-gradle-plugin" "google,central"
report_line "secrets-gradle-plugin" "$(get_catalog_version secrets-gradle-plugin)" "com.google.android.libraries.mapsplatform.secrets-gradle-plugin" "secrets-gradle-plugin" "google,central,gradleportal"
report_line "gradle-wrapper" "$(get_wrapper_version)" "gradle" "gradle-current" "gradlecurrent"

# Libraries
report_line "core-ktx" "$(get_catalog_version coreKtx)" "androidx.core" "core-ktx" "google,central"
report_line "junit" "$(get_catalog_version junit)" "junit" "junit" "central"
report_line "androidx-test-junit" "$(get_catalog_version junitVersion)" "androidx.test.ext" "junit" "google,central"
report_line "espresso-core" "$(get_catalog_version espressoCore)" "androidx.test.espresso" "espresso-core" "google,central"
report_line "lifecycle-runtime-ktx" "$(get_catalog_version lifecycleRuntimeKtx)" "androidx.lifecycle" "lifecycle-runtime-ktx" "google,central"
report_line "activity-compose" "$(get_catalog_version activityCompose)" "androidx.activity" "activity-compose" "google,central"
report_line "compose-bom" "$(get_catalog_version composeBom)" "androidx.compose" "compose-bom" "google,central"
report_line "navigation" "$(get_catalog_version navigation)" "androidx.navigation" "navigation-compose" "google,central"
report_line "kotlinx-serialization-json" "$(get_catalog_version kotlinx-serialization)" "org.jetbrains.kotlinx" "kotlinx-serialization-json" "central"
report_line "datastore-preferences" "$(get_catalog_version datastore)" "androidx.datastore" "datastore-preferences" "google,central"
report_line "room-runtime" "$(get_catalog_version room)" "androidx.room" "room-runtime" "google,central"
report_line "work-runtime-ktx" "$(get_catalog_version workmanager)" "androidx.work" "work-runtime-ktx" "google,central"
report_line "coil-compose" "$(get_catalog_version coil)" "io.coil-kt" "coil-compose" "central"
report_line "retrofit" "$(get_catalog_version retrofit)" "com.squareup.retrofit2" "retrofit" "central"
report_line "okhttp" "$(get_catalog_version okhttp)" "com.squareup.okhttp3" "okhttp" "central"
report_line "gson" "$(get_catalog_version gson)" "com.google.code.gson" "gson" "central"
report_line "appcompat" "$(get_catalog_version appcompat)" "androidx.appcompat" "appcompat" "google,central"
report_line "material" "$(get_catalog_version material)" "com.google.android.material" "material" "google,central"
report_line "constraintlayout" "$(get_catalog_version constraintlayout)" "androidx.constraintlayout" "constraintlayout" "google,central"
report_line "fragment-ktx" "$(get_catalog_version fragment)" "androidx.fragment" "fragment-ktx" "google,central"
report_line "play-services-location" "$(get_catalog_version play-services-location)" "com.google.android.gms" "play-services-location" "google,central"
report_line "play-services-maps" "$(get_catalog_version play-services-maps)" "com.google.android.gms" "play-services-maps" "google,central"
report_line "maps-compose" "$(get_catalog_version maps-compose)" "com.google.maps.android" "maps-compose" "google,central"
