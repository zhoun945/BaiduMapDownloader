package com.bfd.map;

import java.math.BigDecimal;

/**
 * @author zhoun945@gmail
 * @Date 2018-03-27
 */
public class Test {

  // 参考文章：http://www.cnblogs.com/cglNet/archive/2013/11/26/3443637.html
  // 平均一个文件5K
  // 前15级总和2.3T
  // 16级 1531626496 总和7.1T
  public static void main(String[] args) {
    long total = 0;
    for (int z = 3; z <= 14; z++) {
      double r = 20037508.34278924;
      double px = Math.pow(2, (18 - z));
      double mx = r / (px * 256);
      int n = new BigDecimal(Math.ceil(mx)).intValue();
      int m = 0 - n;
      System.out.println(m);

      long num = new BigDecimal(Math.pow(n * 2, 2)).longValue();
      total += num;
    }
    System.out.println(total);
  }

}
