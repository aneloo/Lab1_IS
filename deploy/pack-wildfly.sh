

set -euo pipefail

SRC="${1:-$HOME/wildfly}"
OUT="${2:-$HOME/wildfly-helios.tar.gz}"
TMP="$(mktemp -d)"

echo "Копирую $SRC ..."
rsync -a \
  --exclude 'standalone/log/*' --exclude 'standalone/tmp/*' --exclude 'standalone/data/*' \
  --exclude 'standalone/deployments/*' --exclude 'standalone/configuration/standalone_xml_history' \
  --exclude 'domain' --exclude 'docs/examples' --exclude '.DS_Store' \
  --exclude 'modules/org/postgresql' \
  "$SRC/" "$TMP/wildfly/"


if [ -f "$SRC/standalone/configuration/standalone_xml_history/standalone.initial.xml" ]; then
  cp "$SRC/standalone/configuration/standalone_xml_history/standalone.initial.xml" \
     "$TMP/wildfly/standalone/configuration/standalone.xml"
fi
mkdir -p "$TMP/wildfly/standalone/deployments" "$TMP/wildfly/standalone/log"

tar -czf "$OUT" -C "$TMP" wildfly
rm -rf "$TMP"
echo "Готово: $OUT ($(du -h "$OUT" | cut -f1))"
