@ECHO off
color a
ECHO enter commmit tag
set /p msg=_
color b
git add --all
color c
git commit -m "%msg%"
color d
git push https://github.com/H0rologium/SimpleMediaPlayer master
color e
pause
exit /b