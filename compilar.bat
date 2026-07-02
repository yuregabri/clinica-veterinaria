@echo off
echo ========================================
echo   Compilando e executando o sistema
echo   Clinica Veterinaria - POO
echo ========================================
echo.

REM Define o diretorio atual onde o .bat esta sendo executado
set "PROJECT_DIR=%~dp0"

REM Remove a barra invertida final se existir
if "%PROJECT_DIR:~-1%"=="\" set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"

echo Diretorio do projeto: %PROJECT_DIR%
echo.

REM Verifica se a pasta src existe
if not exist "%PROJECT_DIR%\src" (
    echo ERRO: Pasta 'src' nao encontrada!
    echo Certifique-se de que o .bat esta na raiz do projeto.
    pause
    exit /b 1
)

REM Verifica se a pasta out existe, se nao, cria
if not exist "%PROJECT_DIR%\out" (
    echo Criando pasta 'out'...
    mkdir "%PROJECT_DIR%\out"
)

echo Compilando os arquivos Java...
echo.

REM Compilacao com encoding UTF-8 para evitar problemas de acentos
javac -encoding UTF-8 -d "%PROJECT_DIR%\out" -sourcepath "%PROJECT_DIR%\src" "%PROJECT_DIR%\src\com\clinica\Main.java"

REM Verifica se a compilacao foi bem sucedida
if %errorlevel% neq 0 (
    echo.
    echo ERRO: Falha na compilacao!
    echo Verifique se o JDK esta instalado e configurado corretamente.
    pause
    exit /b 1
)

echo.
echo Compilacao concluida com sucesso!
echo Executando o sistema...
echo.

REM Executa o programa
java -cp "%PROJECT_DIR%\out" com.clinica.Main

REM Verifica se a execucao foi bem sucedida
if %errorlevel% neq 0 (
    echo.
    echo ERRO: Falha na execucao!
    pause
    exit /b 1
)

pause