package com.core.tools;

import com.robert.vesta.service.impl.IdServiceImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class CategoriesBuilder {
    static String[] genders = {"男","女","未知"};
    static String[] categories = {"外套","衬衫","毛衣","牛仔裤","休闲鞋","钱包","跨包","腕表","眼镜","手机","笔记本","电视","冰箱","洗衣机","空调"};
    static String[] colors = {"黑色","白色","红色","粉色","灰色","蓝色","绿色","银白色","金色","玫瑰金","梦幻紫","棕色","碧玉青","青色","黄色","天蓝色","卡其蓝","月牙白","琥珀红"};
    static  int[] cyears = {2015,2016,2017};
    static int[] months = {1,2,3,4,5,6,7,8,9,10,11,12};
    static int[] days = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28};
    static int[] times = {
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
            21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,
            41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59};

    static Map<String,String[]> getCatBrandsInfo() {
        Map<String,String[]> map =  new HashMap<String,String[]>();
        map.put("外套", new String[]{
                "didiboy","holister","evan dress","coach","prich","old navy","tennie weenie","cache","PUMA",
                "guess","gap","enc","basic house","千趣会","韩都衣舍","弋阳","boss","有贷","非池中","dusty"});
        map.put("衬衫", new String[]{
                "jack jones","selected","GXG","太平鸟","温馨鸟","雅戈尔","无印良品","hazy","红豆"});
        map.put("毛衣", new String[]{
                "简诗菲","MISS SIXTY","恒源祥","恩裳","inman","erdos","报喜鸟","PORTS","jack jones",
                "七匹狼","劲霸男装","才子","太平鸟"});
        map.put("牛仔裤", new String[]{
                "ONLY","HLA","ZARA","太平鸟","LEVIS","韩都衣舍","弋阳","jack jones","Lee","GXG","七匹狼","CAMEL"});
        map.put("休闲鞋", new String[]{
                "ECCO","adidas","JORDAN","PUMA","锐步","安踏","特步","VANS","LINING","花花公子",
                "Nike","BeLLE","CAMEL","PLAYBOY","奥康","意尔康","沙驰","SENDA","七匹狼"});
        map.put("钱包", new String[]{
                "COACH","Kipling","FOSSIL","GUCCL","PLAYBOY","GUCCI","皮尔卡丹","梦娇特","稻草人",
                "FOSSIL","ZARA","PARDA","Michael Kors"});
        map.put("跨包", new String[]{
                "ELLE","COACH","Kipling","Michael Kors","VANESSA HOGAN","Dissona","红谷","PARDA","FION",
                "金利来","稻草人","JONBAG","GUESS","FOXER"});
        map.put("腕表", new String[]{
                "泰格豪雅","天梭","浪琴","卡西欧","西铁城","卡地亚","阿玛尼","聚利时","CK","Folli","DW","Levi's",
                "Nomos","博朗","朗坤","天王","飞亚达","海鸥","罗西尼","格雅","冠琴","GUCCI","COACH","CK","POLICE"});
        map.put("眼镜", new String[]{
                "ZENNI","GUCCI","COACH","Bolon","Levi's","CK","依视路","Seiko","JINS","PARDA","帕莎","莫森"});
        map.put("手机", new String[]{"苹果","华为","小米","三星","联想","vivo","魅族","oppo"});
        map.put("笔记本", new String[]{"苹果","戴尔","联想","惠普","华硕","ThinkPad","华为","小米","微星","宏碁","神州"});
        map.put("电视", new String[]{"夏普","三星","LG","飞利浦","索尼","海信","创维","康佳","长虹","乐华","海尔","熊猫","东芝"});
        map.put("冰箱", new String[]{"海尔","美的","西门子","松下","志高","Little Duck","奥克斯","Little Swan",
                "Bosch","海信","LG","创维","格兰仕"});
        map.put("洗衣机", new String[]{"海尔","美的","西门子","松下","志高","Little Duck","奥克斯","Little Swan",
                "Bosch","海信","LG","创维","格兰仕"});
        map.put("空调", new String[]{"格力","美的","奥克斯","海尔","松下","海信","小米","大金","日立","科龙","富士通"});
        return map;
    }
    static Map<String,String[]> getbrandProductInfo(){
        Map<String,String[]> map =  new HashMap<String,String[]>();
        map.put("外套", new String[]{
                "运动","休闲","春新品","夏新品","秋新品","冬新品","保暖","潮流","韩版",
                "商务","复古","青春","学生","简约","英伦","商务休闲","运动休闲","商务潮流","冬新品 保暖","韩版 休闲"});
        map.put("衬衫", new String[]{
                "休闲 V领 短袖","休闲百搭 翻领 短袖","韩版 POLO领 短袖","韩版休闲 圆领 短袖","秋季 方领 长袖","夏季 尖领 长袖","春季 V领 短袖","冬季 翻领 长袖","商务 翻领 长袖"});
        map.put("毛衣", new String[]{
                "秋季 针织衫 宽松 长袖","春季 韩版 修身 短袖","潮流 宽松","高领 修身","冬季新品 爆款","保暖","圆领 套头","打底 保暖 ","新品潮流 修身",
                "七匹狼","劲霸男装","才子","太平鸟"});
        map.put("牛仔裤", new String[]{
                "秋季新品 修身 中腰","夏季新品 直筒 中腰","复古 低腰","韩版 喇叭裤 低腰 ","修身 直筒裤 中腰","春季新款 背带裤 低腰","修身裤 中低腰"});
        map.put("休闲鞋", new String[]{
                "休闲 真皮","韩版 人造革","简约 棉质","英伦 超纤","优雅 布面","民族 网纱","舒适 棉质","春季新品 休闲 超纤","冬季 韩版 布面","休闲 网纱","韩版 帆布","简约 舒适","英伦 棉质"});
        map.put("钱包", new String[]{
                "长款 拉链 商务 PU","短款 拉链搭扣 牛皮","应季每包 帆布 日韩风范","牛仔布 敞口 复古风","PVC 挂钩 欧美","鳄鱼皮 拉链 欧美","锦纶 挂钩 商务/OL","牛皮 搭扣 日韩风范"});
        map.put("跨包", new String[]{
                "长款 拉链 商务 PU","短款 拉链搭扣 牛皮","应季每包 帆布 日韩风范","牛仔布 敞口 复古风","PVC 挂钩 欧美","鳄鱼皮 拉链 欧美","锦纶 挂钩 商务/OL","牛皮 搭扣 日韩风范"});
        map.put("腕表", new String[]{
                "石英 休闲 夜光","机械 时尚 防水","手动机械 商务 陀飞轮","机械 复古 防磁","石英 商务 月份显示","机械 休闲 日期显示","智能 运动 万年历","石英 时尚 夜光"});
        map.put("眼镜", new String[]{
                "近视镜 休闲","老花镜 复古","太阳镜 潮流","游泳镜 休闲","装饰镜 休闲","防辐射 休闲","防蓝光 休闲","近视镜 复古","老花镜 潮流","太阳镜 运动"});
        map.put("手机", new String[]{"A10 内存8GB 高清HD 4.5英寸 四核","A7 内存16GB 全高清FHD 5.2英寸 六核","X10 内存64GB 高清HD 4.8英寸 四核",
                "X8 内存128GB 高清HD 5.6英寸 六核","H10 内存64GB 高清HD 5.2英寸 四核","H2 内存64GB 高清HD 5.2英寸 四核",
                "PLUS 内存256GB 高清HD 6.0英寸 六核","PLUS++ 内存512GB 高清HD 6.0英寸 四核","P1 内存64GB 高清HD 4.8英寸 四核",
                "P2 内存64GB 高清HD 4.8英寸 四核","P3 内存64GB 高清HD 4.8英寸 四核","G3 内存64GB 高清HD 4.8英寸 四核","G4 内存64GB 高清HD 4.8英寸 四核",
                "C10 内存64GB 高清HD 4.8英寸 四核","C11 内存256GB 高清HD 5.8英寸 四核","C12 内存128GB 高清HD 5.2英寸 四核"});
        map.put("笔记本", new String[]{"A6125 15.6英寸 金属 8G 256G FHD 128GSSD ","30010 14.8英寸 金属 8G 256G","Y7000 15.6英寸 16G 1T",
                "游匣G3烈焰版 15.6英寸 8G 1T","灵越燃700 15.6英寸 16G 1T","灵越5000 15.6英寸 8G 1T","Precision移动工作站 15.6英寸 16G 1T 256GSSD",
                "灵耀s 15.6英寸 8G 512G", "飞行堡垒5 15.6英寸 16G 1T","Air13 15.6英寸 16G 1T","暗影精灵 15.6英寸 16G 1T"});
        map.put("电视", new String[]{"曲面 55英寸 4k超清","超薄 48英寸 高清","曲面 58英寸  8k超清","曲面 48英寸 全高清",
                "人工智能 55英寸  4k超清","曲面 65英寸 8k超清","超薄 55英寸 高清","曲面 78英寸 4k超清"});
        map.put("冰箱", new String[]{"对开门 无霜 电脑控温","十字对开门 混冷 电脑控温","多门 直冷 电脑控温","三门 无霜 机械控冷","双门 混冷 机械控冷",
                "单门 直冷 机械控冷", "对开门 直冷 电脑控温","十字对开门 无霜 机械控冷","多门 直冷 电脑控温","三门 无霜 机械控冷","双门 混冷 电脑控温","单门 无霜 电脑控温"});
        map.put("洗衣机", new String[]{"定频 母婴洗","变频 母婴洗","定频 滚筒","变频 滚筒","定频 mini","变频 mini","定频 洗烘一体","变频 洗烘一体","定频 双缸","变频 双缸"});
        map.put("空调", new String[]{"定额 1匹 壁挂式 冷暖空调","变频 1匹 壁挂式 单冷空调","定额 1.5匹 立柜式 冷暖空调","变频 1.5匹 壁挂式  单冷空调","定额 2匹 立柜式 冷暖空调","变频 2匹 壁挂式 单冷空调","定额 2.5匹 立柜式 冷暖空调","变频 2.5匹 壁挂式 单冷空调","定额 3匹 立柜式 冷暖空调","变频 3匹 壁挂式 单冷空调"});
        return map;
    }

    static Map<String,String[]> getSizeInfo(){
        Map<String,String[]> map =  new HashMap<String,String[]>();
        map.put("外套", new String[]{"S","M","L","X","XL","2XL","3XL"});
        map.put("衬衫",  new String[]{"S","M","L","X","XL","2XL","3XL"});
        map.put("毛衣",  new String[]{"S","M","L","X","XL","2XL","3XL"});
        return map;
    }

    public void build() {
        IdGeneratorConfig generator = new IdGeneratorConfig();
        IdServiceImpl idService = generator.idService();
        Random random = new Random();
        try {
            File file = new File("E:\\catalog.csv");
            StringBuilder header = new StringBuilder();
            header.append("id,").append("catName,").append("brand,").append("productName,").append("size,")
                    .append("color,").append("gender,").append("price,").append("review,")
                    .append("createTime,").append("updateTime,").append("instock,").append("onsale,").append("new").append("\r\n");
            FileUtils.write(file,header.toString(),Charset.forName("GB2312"),true);
            int count = 0;

            while(count < 400000) {
                int c = random.nextInt(categories.length);
                String cat = categories[c];
                List<Model> models = new ArrayList<Model>();
                for (String brand : getCatBrandsInfo().get(cat)) {
                    Model model = new Model();
                    long id = idService.genId();
                    model.setId(id);
                    model.setCategoryName(cat);
                    model.setBrand(brand);
                    models.add(model);
                }

                for (int i = 0; i < models.size(); i++) {
                    String[] elements = getbrandProductInfo().get(cat);
                    int pos = random.nextInt(elements.length);
                    models.get(i).setProductName(models.get(i).getBrand()+" "+elements[pos]);
                }

                for (int i = 0; i < models.size(); i++) {
                    String[] elements = getSizeInfo().get(cat);
                    if (elements == null) continue;
                    int pos = random.nextInt(elements.length);
                    if (elements[pos] == null || elements[pos].equals("")) {
                        continue;
                    }
                    models.get(i).setSize(elements[pos]);
                }

                for (int i = 0; i < models.size(); i++) {
                    int pos = random.nextInt(colors.length);
                    models.get(i).setColor(colors[pos]);
                }

                for (int i = 0; i < models.size(); i++) {
                    int pos = random.nextInt(genders.length);
                    models.get(i).setGender(genders[pos]);
                }

                for (int i = 0; i < models.size(); i++) {
                    int yearIndex = random.nextInt(cyears.length);
                    String month = String.valueOf(random.nextInt(2))+String.valueOf(random.nextInt(2)+1);
                    String day =  String.valueOf(random.nextInt(3))+String.valueOf(random.nextInt(9)+1);
                    String hour = String.valueOf(random.nextInt(2))+String.valueOf(random.nextInt(9)+1);
                    String minute = String.valueOf(random.nextInt(6))+String.valueOf(random.nextInt(9)+1);
                    String second =String.valueOf(random.nextInt(6))+String.valueOf(random.nextInt(9)+1);
                    String createTime = cyears[yearIndex]+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
                    models.get(i).setCreateTime(createTime);
                    String updateTime = "2018-"+month+"-"+day+" "+hour+":"+minute+":"+second;
                    models.get(i).setUpdateTime(updateTime);
                }

                for (int i = 0; i < models.size(); i++) {
                    boolean outstock = i % 10000 == 0;
                    boolean onsale = i % 5000 == 0;
                    boolean isnew = i % 8000 == 0;
                    models.get(i).setInstock(outstock ? false : true);
                    if (outstock) {
                        models.get(i).setOnsale(false);
                        models.get(i).setRefresh(false);
                    } else {
                        models.get(i).setOnsale(onsale);
                        models.get(i).setRefresh(isnew);
                    }
                }

                for (int i = 0; i < models.size(); i++) {
                    models.get(i).setReview(random.nextInt(1000000));
                }
                for (int i = 0; i < models.size(); i++) {
                    long price = random.nextInt(1000000)+500;
                    if (models.get(i).isOnsale())
                        price = (long)(price * 0.8);
                    models.get(i).setPrice(price);
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < models.size(); i++) {
                    sb.append(models.get(i).getId()).append(",");
                    sb.append(models.get(i).getCategoryName()).append(",");
                    sb.append(models.get(i).getBrand()).append(",");
                    sb.append(models.get(i).getProductName()).append(",");
                    sb.append(models.get(i).getSize()).append(",");
                    sb.append(models.get(i).getColor()).append(",");
                    sb.append(models.get(i).getGender()).append(",");
                    sb.append(models.get(i).getPrice()).append(",");
                    sb.append(models.get(i).getReview()).append(",");
                    sb.append(models.get(i).getCreateTime()).append(",");
                    sb.append(models.get(i).getUpdateTime()).append(",");
                    sb.append(models.get(i).isInstock()).append(",");
                    sb.append(models.get(i).isOnsale()).append(",");
                    sb.append(models.get(i).isRefresh());
                    sb.append("\r\n");
                    FileUtils.write(file,sb.toString(),Charset.forName("GB2312"),true);
                    sb.delete(0,sb.length());
                }
                models = null;
                count++;
                System.out.println(count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new CategoriesBuilder().build();
    }
}
