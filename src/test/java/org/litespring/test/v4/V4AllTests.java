package org.litespring.test.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @Author: wangjunjie 2018/7/31 17:34
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2018/7/31 17:34
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationContextTest4.class,ClassPathBeanDefinitionScannerTest.class,ClassReaderTest.class,
DependencyDescriptorTest.class,MetadataReaderTest.class,PackageResourceLoaderTest.class,XmlBeanDefinitionReaderTest.class})
public class V4AllTests {
}
