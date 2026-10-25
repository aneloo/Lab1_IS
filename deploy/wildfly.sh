

WILDFLY_HOME="${WILDFLY_HOME:-$HOME/wildfly}"

PORT_OFFSET="${PORT_OFFSET:-17733}"

JAVA_MEM="${JAVA_MEM:--Xmx512m -XX:MaxMetaspaceSize=256m}"

HTTP_PORT=$((8080 + PORT_OFFSET))
MGMT_PORT=$((9990 + PORT_OFFSET))
LOG="$WILDFLY_HOME/standalone/log/server.log"

start() {
  if pgrep -u "$(id -u)" -f "jboss-modules.jar" >/dev/null; then
    echo "WildFly уже запущен"; return
  fi
  local before
  before=$(wc -l < "$LOG" 2>/dev/null || echo 0)
  echo "Запуск WildFly: http://localhost:$HTTP_PORT/is-lab1/ (management $MGMT_PORT)"
  _JAVA_OPTIONS="$JAVA_MEM" nohup "$WILDFLY_HOME/bin/standalone.sh" \
      -Djboss.socket.binding.port-offset="$PORT_OFFSET" > "$HOME/wildfly.out" 2>&1 &
  for _ in $(seq 1 60); do
    sleep 2
    if tail -n +"$((before + 1))" "$LOG" 2>/dev/null | grep -q "WFLYSRV0025\|WFLYSRV0026"; then
      tail -n +"$((before + 1))" "$LOG" | grep "WFLYSRV0025\|WFLYSRV0026\|WFLYSRV0010\|ERROR" | tail -5
      return
    fi
  done
  echo "Сервер не стартовал за 2 минуты, смотрите $LOG и $HOME/wildfly.out"
}

stop() {
  "$WILDFLY_HOME/bin/jboss-cli.sh" --connect --controller="localhost:$MGMT_PORT" command=:shutdown \
    || pkill -u "$(id -u)" -f "jboss-modules.jar"
}

case "$1" in
  start)   start ;;
  stop)    stop ;;
  restart) stop; sleep 3; start ;;
  status)  pgrep -u "$(id -u)" -fl "jboss-modules.jar" || echo "не запущен" ;;
  log)     tail -n 100 -f "$LOG" ;;
  *)       echo "Использование: $0 start|stop|restart|status|log" ;;
esac
