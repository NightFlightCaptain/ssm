package common;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis-Genetator，在generator-config中配置好要生成文件的数据表然后执行这里
 *
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/5/31 11:04
 */
public class MybatisGenerator {

	/**
	 * mybatis-generator自动生成
	 * @param args
	 * @throws IOException
	 * @throws XMLParserException
	 * @throws InvalidConfigurationException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
		///H:/java_world/seckill/target/classes/
		System.out.println(MybatisGenerator.class.getClass().getResource("/").getPath());
		generator();
	}
	private static void generator() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File(MybatisGenerator.class.getClass().getResource("/").getPath()+ "config/generator-config.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}
}
