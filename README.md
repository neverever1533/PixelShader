# EvolutionVector
一些个人用的图形化小工具

---------分割线---------

说明：
    本项目由 eclipse java-2022-03 版本创建，项目分别导出 OpenJDK JavaSE-17 和 JavaSE-1.8 两个运行环境的 jar 文件，若两个版本 jar 均运行出错或并未安装 Java 环境，请使用以下方法以体验本项目相关小工具：

1. 安装对应版本 Java 安装包（未安装Java环境的用户）：

 1.1 我的电脑右键【属性】 > 【高级系统设置】 > 【环境变量】 > 【系统变量】 > 【新建】 > 【变量名】
 
 JAVA_HOME
 
【浏览目录】

例：【变量值】

D:\Application\Java\jdk1.8.0_201

![17](https://user-images.githubusercontent.com/52105884/161420296-5c066787-c309-4ed8-a723-9ef3d3da3284.PNG)

 1.2 【系统变量】 > 【path】 > 【编辑】 > 【新建】

%JAVA_HOME%\bin

![18](https://user-images.githubusercontent.com/52105884/161420310-5da77ec6-9f9d-47c2-ace7-e629233562b4.PNG)

2. 放置对应 Java 版本的完整的 JRE 文件夹（不想安装java或当期版本不适用可用此方法）：

JRE 目录存放到项目 jar 所在的文件夹，新建文本文档并编辑，输入以下内容：

start jre\bin\javaw -jar TestUI_for_JavaSE-1.8.jar

另存为或重命名 txt 文件为 TestUI_for_JavaSE-1.8_run.bat，然后鼠标双击此 bat 文件就能运行 jar 项目文件；

![16](https://user-images.githubusercontent.com/52105884/161420245-c6d7ce51-3a48-4d6d-b031-98d155a175dd.PNG)

---------分割线---------

项目计划：

计划一：
 树状文件加载测试>（进度95%）

 1. 测试用图形化界面：

![00](https://user-images.githubusercontent.com/52105884/159549114-12750b14-ee30-4e7b-bc71-1590ece97b03.PNG)

 2. 测试文件浏览

  2.1 浏览文件，单选、多选（文件夹、文件）

![01](https://user-images.githubusercontent.com/52105884/159549120-72453c7a-7f84-4e9c-9bf7-f3cc7bc14699.PNG)

  2.2 文件加载入树

![02](https://user-images.githubusercontent.com/52105884/159549127-457693f3-355b-4817-9787-e65133eef4b3.PNG)

 3. 测试文件拖拽

![03](https://user-images.githubusercontent.com/52105884/159549138-4524a100-bb71-48a8-9ca5-8f83351d2706.PNG)

 4. 测试树

  4.1 树节点展开（自定义文件夹类型 快捷：鼠标左键双击）

![10](https://user-images.githubusercontent.com/52105884/161045369-6310a975-0576-412e-9051-249796cf4271.PNG)
![04](https://user-images.githubusercontent.com/52105884/159549140-83afcb1d-e003-4b5c-a89e-b25b3758b92e.PNG)

  4.2 测试树节点拖拽

![06](https://user-images.githubusercontent.com/52105884/161034999-3f2c05e2-db6e-4dbf-aaec-d60daa05dfb3.PNG)
![07](https://user-images.githubusercontent.com/52105884/161035028-5fcdbb9b-6779-473b-aacc-6be7cbace801.PNG)
![08](https://user-images.githubusercontent.com/52105884/161035088-64cd1f44-194e-4503-970c-0dbc1d0de4e4.PNG)
![11](https://user-images.githubusercontent.com/52105884/161241457-cdca7213-b077-4fa3-937b-542010571132.PNG)
![12](https://user-images.githubusercontent.com/52105884/161241490-d84146a0-7483-4d61-ad6f-ddc231566d04.PNG)

  4.3 测试树节点删除（快捷：鼠标右键单击）

![09](https://user-images.githubusercontent.com/52105884/161035221-6b487a1d-22e9-40b5-b048-dc3748a52032.PNG)

  
计划二：
    图形化界面优化>（进度80%）

 1. 预设功能（进度15%）

  1.1 文件

   1.1.1 打开（已完成）

   1.1.2 拖拽（已完成）

  1.2 编辑

   1.2.1 合并导出（已完成）

   1.2.2 变形（进度35%）

![05](https://user-images.githubusercontent.com/52105884/159551456-e9c63e3e-303d-4a63-a635-f522ab3e0fb3.PNG)


计划三：

 图层合并功能实装>（已完成）
 
 图层变形功能实装>（进度35%）
    
 ......待编辑......
    
