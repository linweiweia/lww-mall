package com.ww.mall.tiny.mbg;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-24 17:59
 * @describe:   配置MyBaits Generator 自动生成实体添加自定义注释
 */
public class CommentGenerator extends DefaultCommentGenerator {

    //查看父类源码是是否添加数据库内的注释 true为是
    private boolean addRemarkComments = false;
    private static final String EXAMPLE_SUFFIX="Example";
    private static final String API_MODEL_PROPERTY_FULL_CLASS_NAME="io.swagger.annotations.ApiModelProperty";

    /**
     * 1.设置（获取）用户配置的参数
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        //因为在generatorConfig中设置了为true，所以这里都是true
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
    }


    /**
     * 1.应该是选择要添加的注释的文件  这里是javabean才可以
     * @param compilationUnit
     */
    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        super.addJavaFileComment(compilationUnit);
        //只在model中添加swagger注解类的导入 例如不想在PmsBrandExample生成，就写一个EXAMPLE_SUFFIX类名关键字做判断
        if(!compilationUnit.isJavaInterface()&&!compilationUnit.getType().getFullyQualifiedName().contains(EXAMPLE_SUFFIX)){
            compilationUnit.addImportedType(new FullyQualifiedJavaType(API_MODEL_PROPERTY_FULL_CLASS_NAME));
        }
    }

    /**
     * 2.给字段添加swagger注释
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        //根据参数和备注信息判断是否添加备注信息addRemarkComments都是为true
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
            //addFieldJavaDoc(field, remarks);
            //数据库字段备注中特殊字符做转义处理
            if (remarks.contains("\"")) {
                remarks = remarks.replace("\"","'");
            }
            //给实体类字段添加swagger注解
            field.addJavaDocLine("@ApiModelProperty(value = \""+remarks+"\")");
        }
    }



    private void addFieldJavaDoc(Field field, String remarks) {
        //文档注释开始
        field.addJavaDocLine("/**");
        //获取数据库字段的备注信息
        String[] remarkLines = remarks.split(System.getProperty("line.separator"));
        for (String remarkLine : remarkLines) {
            field.addJavaDocLine(" * " + remarkLine);
        }
        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }
}
