#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(git rev-parse --show-toplevel)"
cd "$ROOT_DIR"

timestamp="$(date +%Y%m%d-%H%M%S)"
latest_report="reports/latest-versions-${timestamp}.md"
audit_report="reports/history-build-audit-${timestamp}.tsv"

mkdir -p reports

./scripts/latest_versions_report.sh | tee "$latest_report"
./scripts/rewrite_history_with_current_versions.sh
RUN_GRADLE_HELP=1 RUN_LINT=1 ./scripts/audit_build_history.sh "$audit_report"

echo
echo "Latest versions report: $latest_report"
echo "History audit report: $audit_report"
