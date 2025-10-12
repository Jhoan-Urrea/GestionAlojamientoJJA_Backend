@echo off
setlocal ENABLEDELAYEDEXPANSION
REM Ejecuta la suite de tests y muestra los reportes de surefire

cd /d %~dp0

REM Forzar perfil de pruebas (H2 en memoria)
set SPRING_PROFILES_ACTIVE=test

if "%~1"=="" (
  set MVNARGS=clean test
  echo Running full test suite...
) else (
  set MVNARGS=-Dtest=%~1 test
  echo Running selected tests: %~1
)

call mvnw.cmd -B %MVNARGS%
set RESULT=%ERRORLEVEL%

if not exist target\surefire-reports (
  echo [WARN] No se encontró target\surefire-reports. Es posible que la build haya fallado antes de ejecutar tests.
  exit /b %RESULT%
)

echo.
echo ==== Surefire reports (resumen) ====
for %%f in (target\surefire-reports\*.txt) do (
  echo -----------------------------
  echo Report: %%~nxf
  for /f "tokens=1,* delims=" %%l in ("%%f") do (
    set line=%%l
    echo !line!
  )
)

REM Intentar detectar fallos a partir de XML (opcional)
for %%x in (target\surefire-reports\TEST-*.xml) do (
  findstr /c:"failures=\"0\"" /c:"errors=\"0\"" "%%x" >nul
  if errorlevel 1 (
    echo [INFO] Revisa: %%~nxx
  )
)

echo.
if %RESULT% NEQ 0 (
  echo [FAIL] Tests fallidos. Codigo de salida: %RESULT%
) else (
  echo [OK] Todos los tests pasaron.
)
exit /b %RESULT%

