# EvolutionVector
一些个人用的图形化小工具

---------分割线---------

说明：

  本项目由 eclipse java-2022-03 版本创建，安装有 eclipse 类软件的可使用项目源码打包使用，不会打包项目的请下载

  ① PixelShaderUI_for_JavaSE-1.8_v.40.3.jar；

  ② lib 文件夹（其内 cn.imaginary.toolkit.v3.6.jar 和psd-library-001.jar 为项目依赖包）；

  psd文件java支持库来源:http://code.google.com/p/java-psd-library/

  并未安装 Java 环境或运行出错，请使用以下方法获得 Java 环境支持以体验本项目相关小工具：

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

![16](https://user-images.githubusercontent.com/52105884/161420388-a620bbf2-6efc-4970-abde-5d1d5e230236.PNG)

---------分割线---------

项目预览：

![pb-1](https://user-images.githubusercontent.com/52105884/168308808-2872eb73-5346-42a4-bf1a-355ba6244089.PNG)

![pb](https://user-images.githubusercontent.com/52105884/168317856-f617afc1-c057-477a-8684-2d4a3e1784d5.png)

ps.此看板娘psd小人儿作者：【脆皮酥炸蜜汁金黄炸鸡腿】

---------分割线---------

项目计划：

计划五：

支持更新（25%）（这个psd库比较旧了，部分psd读取后丢失图层，有时间再替换掉）

图形算法（15%）

 像素扭曲功能待加入

---------分割线---------

计划四：

 1. 联动继承（进度80%）
 
  父子关系已由树状JTree接管，图层分组后可同步位移或相对旋转；

![pb-4](https://user-images.githubusercontent.com/52105884/168302933-9ad8684f-0e3a-4996-b27b-8556a419376a.PNG)

![pb-0](https://user-images.githubusercontent.com/52105884/168317591-067db83f-9706-4d00-8fd9-71f708273d2d.PNG)

 2. 图帧编辑（进度60%）

  基础的位移，旋转已实现，待加入图片扭曲；
 
![20](https://user-images.githubusercontent.com/52105884/165331163-fb80a4d5-7de4-44de-9e16-13a465eee20c.PNG)

![21](https://user-images.githubusercontent.com/52105884/165331275-d9c8ff3b-0039-4e7c-b2dc-d6b11ae60679.PNG)

![22](https://user-images.githubusercontent.com/52105884/165331320-cab9e434-e2b6-41e1-ae89-8f30de7e1eb0.PNG)

![23](https://user-images.githubusercontent.com/52105884/165331426-b75559de-d0e8-40ba-b19c-f3aeea26033c.PNG)

 ---------分割线---------

计划三：

 图层合并功能实装>（已完成）
 
 图层变形功能实装>（进度35%）
 
 工具栏功能（进度75%）

  1.1 图层修改（进度85%）

   1.1.1 TextField修改坐标、尺寸（宽高/缩放）、变换、锚点、显示实时修改（进度100%）
 
   1.1.2 鼠标修改坐标、旋转、修改锚点（进度50%）

  2.2 图片编辑（进度30%）
 
......待编辑......

---------分割线---------
 
计划二：

 图形化界面优化>（进度80%）

 1. 菜单功能（进度65%）

  1.1 文件

   1.1.1 打开（已完成）

   1.1.2 拖拽（已完成）

  1.2 编辑

   1.2.1 合并导出（已完成）

   1.2.2 变形（进度35%）

![05](https://user-images.githubusercontent.com/52105884/159551456-e9c63e3e-303d-4a63-a635-f522ab3e0fb3.PNG)

---------分割线---------

计划一：
 树状文件加载测试>（进度100%）

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
