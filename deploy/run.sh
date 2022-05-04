python3 ./srcPython/main.py ./src/main/resources/input/ srcPython/output

if [[ $# -eq 1 ]] ; then
    cat srcPython/output/*.csv
fi

./mvnw spring-boot:run &

wait

exit $?
