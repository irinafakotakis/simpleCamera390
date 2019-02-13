# file name and relative path to custom lint rules
CUSTOM_LINT_FILE="lint_rules/lint.jar"

# set directory of custom lint .jar
export ANDROID_LINT_JARS=$(pwd)/$CUSTOM_LINT_FILE

# run lint
./gradlew clean lint