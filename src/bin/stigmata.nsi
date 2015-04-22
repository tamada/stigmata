;--------- CONFIGURATION ---------

!define VERSION "2.0.0"
!define APPNAME "Stigmata: Java birthmarking toolkit"
!define JARFILE "stigmata-${VERSION}.jar"
!define LANG_ENGLISH "1033-English"

;Uncomment the next line to specify an icon for the EXE.
Icon "stigmata.ico"

;Uncomment the next line to specify a splash screen bitmap.
;!define SPLASH_IMAGE "splash.bmp"

;---------------------------------

Name "Stigmata"
Caption "${APPNAME}"
OutFile "stigmata.exe"

VIProductVersion "${VERSION}.0"
SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow

VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductName" "Stigmata"
VIAddVersionKey /LANG=${LANG_ENGLISH} "Comments" "Stigmata: Java birthmark toolkit"
VIAddVersionKey /LANG=${LANG_ENGLISH} "CompanyName" "Kyoto Sangyo University, Japan"
VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductVersion" "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalCopyright" "Copyright (C) 2004-2008 by Haruaki Tamada"
VIAddVersionKey /LANG=${LANG_ENGLISH} "FileDescription" "Java birthmark toolkit"
VIAddVersionKey /LANG=${LANG_ENGLISH} "FileVersion" "${VERSION}"

!addplugindir .

Section "running-stigmata"
  System::Call "kernel32::CreateMutexA(i 0, i 0, t 'jelude') i .r1 ?e"
  Pop $R0
  StrCmp $R0 0 +2
  Quit

  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  IfErrors 0 FoundVM

  ClearErrors
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R0" "JavaHome"
  IfErrors 0 FoundVM

  ClearErrors
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$R0" "JavaHome"
  IfErrors NotFound 0

  FoundVM:
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfFileExists $R0 0 NotFound

  StrCpy $R1 ""
  Call GetParameters
  Pop $R1

  SetOverwrite ifdiff
  SetOutPath $TEMP
  File "..\..\target\${JARFILE}"
  File "..\..\target\commons-beanutils-1.7.0.jar"
  File "..\..\target\commons-cli-1.1.jar"
  File "..\..\target\commons-dbutils-1.1.jar"
  File "..\..\target\commons-logging-1.0.3.jar"
  File "..\..\target\asm-all-3.1.jar"
  File "..\..\target\xmlcli-1.2.2.jar"
  File "..\..\target\mds-1.0.0.jar"
  File "..\..\target\i18n-1.0.1.jar"
  File "..\..\target\digger-1.1.0.jar"

  StrCpy $R0 '$R0 -Dexecution.directory="$EXEDIR" -jar "${JARFILE}" $R1'

  !ifdef SPLASH_IMAGE
    SetOutPath $TEMP
    File /oname=spltmp.bmp "${SPLASH_IMAGE}"
    Splash::show 4000 "$TEMP\spltmp"
    Delete "$TEMP\spltmp.bmp"
  !endif


  ExecWait "$R0"
  Delete "$TEMP\${JARFILE}"
  Delete "$TEMP\commons-beanutils-1.7.0.jar"
  Delete "$TEMP\commons-cli-1.1.jar"
  Delete "$TEMP\commons-dbutils-1.1.jar"
  Delete "$TEMP\commons-logging-1.0.3.jar"
  Delete "$TEMP\asm-all-3.1.jar"
  Delete "$TEMP\xmlcli-1.2.2.jar"
  Delete "$TEMP\mds-1.0.0.jar"
  Delete "$TEMP\i18n-1.0.1.jar"
  Delete "$TEMP\digger-1.1.0.jar"

  Quit

  NotFound:
  MessageBox MB_ICONEXCLAMATION|MB_YESNO \
          'Could not find a Java Runtime Environment installed on your computer. \
          $\nWithout it you cannot run "${APPNAME}". \
          $\n$\nWould you like to visit the Java website to download it?' \
          IDNO +2
  ExecShell open "http://java.sun.com/getjava"
  Quit
SectionEnd

Function GetParameters
  Push $R0
  Push $R1
  Push $R2
  StrCpy $R0 $CMDLINE 1
  StrCpy $R1 '"'
  StrCpy $R2 1
  StrCmp $R0 '"' loop
  StrCpy $R1 ' '
  loop:
    StrCpy $R0 $CMDLINE 1 $R2
    StrCmp $R0 $R1 loop2
    StrCmp $R0 "" loop2
    IntOp $R2 $R2 + 1
    Goto loop
  loop2:
    IntOp $R2 $R2 + 1
    StrCpy $R0 $CMDLINE 1 $R2
    StrCmp $R0 " " loop2
  StrCpy $R0 $CMDLINE "" $R2
  Pop $R2
  Pop $R1
  Exch $R0
FunctionEnd
