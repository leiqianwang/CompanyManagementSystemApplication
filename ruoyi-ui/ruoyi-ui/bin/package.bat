<<<<<<< HEAD
@echo off
echo.
echo [��Ϣ] ��װWeb���̣�����node_modules�ļ���
echo.

%~d0
cd %~dp0

cd ..
npm install --registry=https://registry.npmmirror.com

=======
@echo off
echo.
echo [��Ϣ] ���Web���̣�����war/jar���ļ���
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -Dmaven.test.skip=true

>>>>>>> 6fd49f5a28d0bf094d3071a13e4fc54149a14b20
pause