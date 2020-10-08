package co.kr.istarbucks.xo.batch.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.dao.XoGiftDao;

/**
 * e-Gift Item ��ȿ���� ������ ����
 * @author LeeJiHun
 */
public class GiftExtinction {

    private static final Logger logger = Logger.getLogger("EGIFT.EXTINCTION");
    private final XoGiftDao xoGiftDao;

    public GiftExtinction() {
        this.xoGiftDao = new XoGiftDao();
    }

    public void start(String compareDate) {
        try {
            logger.info("'��ȿ���� ������ ����' START");
            List<Map<String, Object>> data = xoGiftDao.findExtinction(compareDate);

            if (data == null || data.isEmpty()) {
                logger.info("��ȿ���� ������ ����.");
            }
            else {
                logger.info("��ȿ���� ������ �Ǽ� : {" + data.size() + "}");
                xoGiftDao.saveIcsXoGiftExpList(data);
            }
        }
        catch (Exception e) {
            logger.error("[Exception] e={" + e + "}, cause={" + e.getCause() + "}, msg={" + e.getMessage() + "}");
        }
        finally {
            logger.info("'��ȿ���� ������ ����' FINISH");
        }
    }

    public static void main(String[] args) {
        String date;
        if (args.length == 0) {
            Date yesterday = DateUtils.addDays(new Date(), -1);
            date = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN).format(yesterday);
        }
        else {
            date = args[0];
        }

        new GiftExtinction().start(date);
    }
}
