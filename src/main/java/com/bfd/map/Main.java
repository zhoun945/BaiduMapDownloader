package com.bfd.map;

import com.bfd.map.bean.Position;
import com.bfd.map.bean.Tile;
import com.bfd.map.dowloader.TileDownloader;
import com.bfd.map.util.ConfigUtil;
import com.bfd.map.util.ThreadPool;
import com.bfd.map.util.TileUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhoun945@gmail
 * @Date 2018-03-27
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static BlockingQueue<Tile> tileQueue = new LinkedBlockingQueue<>();
  private static int maxQueueSize = 1000000;
  private static ThreadPool threadPool;
  private static TileDownloader downloader;
  private static int threadSize;

  static {
    threadSize = ConfigUtil.getInstance().getInt("download.thread");
    threadPool = new ThreadPool(threadSize);
    downloader = new TileDownloader();
  }

  public static void main(String[] args) {
    try {
      addQueue();

      while (!threadPool.isShutdown() && !tileQueue.isEmpty()) {
        threadPool.execute(() -> {
          Tile tile = tileQueue.poll();
          if (tileQueue.size() % 1000 == 0) {
            logger.info("下载队列 size：{}", tileQueue.size());
          }
          if (tile != null) {
            downloader.download(tile, 0);
          } else {
            logger.info("本节点任务下载完成");
          }
        });
      }
      threadPool.shutdown();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private static void addQueue() throws InterruptedException {
    String model = ConfigUtil.getInstance().getString("download.scale");
    if (model.equals("world")) {
      new Thread(() -> {
        addWorldTileQueue();
      }).start();
    } else if(model.equals("china")) {
      new Thread(() -> {
        addChinaTileQueue();
      }).start();
    } else {
      logger.error("download.scale 配置错误");
    }
    Thread.sleep(3000);
  }

  private static void addWorldTileQueue() {
    String levelConfig = ConfigUtil.getInstance().getString("download.level");
    List<String> levelList = Arrays.asList(levelConfig.split(","));
    try {
      double r = 20037508.34278924;
      for (String level : levelList) {
        int z = Integer.parseInt(level);
        double px = Math.pow(2, (18 - z));
        double mx = r / (px * 256);
        int n = new BigDecimal(Math.ceil(mx)).intValue();
        int m = 0 - n;

        for (long x = m; x < n; x++) {
          while (tileQueue.size() > maxQueueSize) {
            Thread.sleep(3000);
          }

          for (long y = m; y < n; y++) {
            tileQueue.add(new Tile(z, (int) x, (int) y));
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private static void addChinaTileQueue() {
    try {
      String levelConfig = ConfigUtil.getInstance().getString("download.level");
      String regionConfig = ConfigUtil.getInstance().getString("download.scale.china.region");
      List<String> levelList = Arrays.asList(levelConfig.split(","));
      List<String> regionList = Arrays.asList(regionConfig.split(","));
      List<Position> positionList = TileUtil.getPositionList(regionList);

      for (String level : levelList) {
        int z = Integer.parseInt(level);
        for (Position p : positionList) {
          int sx = TileUtil.getTileNum(p.getSwlng(), z);
          int sy = TileUtil.getTileNum(p.getSwlat(), z);
          int ex = TileUtil.getTileNum(p.getNelng(), z);
          int ey = TileUtil.getTileNum(p.getNelat(), z);

          for (int x = sx; x < ex; x++) {
            while (tileQueue.size() > maxQueueSize) {
              Thread.sleep(3000);
            }
            for (int y = sy; y < ey; y++) {
              tileQueue.add(new Tile(z, x, y));
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

}
