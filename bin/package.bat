<<<<<<< HEAD
@echo off
echo.
echo [信息] 安装Web工程，生成node_modules文件。
echo.

%~d0
cd %~dp0

cd ..
npm install --registry=https://registry.npmmirror.com

=======
@echo off
echo.
echo [信息] 打包Web工程，生成war/jar包文件。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -Dmaven.test.skip=true

>>>>>>> 6fd49f5a28d0bf094d3071a13e4fc54149a14b20
pause