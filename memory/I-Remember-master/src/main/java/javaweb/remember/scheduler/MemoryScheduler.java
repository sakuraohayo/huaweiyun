package javaweb.remember.scheduler;

import javaweb.remember.repository.MemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 每天0点0分0秒进行记忆检索，
 */
@Component
public class MemoryScheduler {
    @Autowired
    MemoryRepository memoryRepository;

    /**
     * 每天凌晨检索所有记忆，如果记忆时间是6个月前则删除
     */
    @Scheduled(cron = "0 0 0 ? * *")
    @Transactional
    public void memoryDisappear(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -6);
        Date nowDate = calendar.getTime();
        memoryRepository.memoryDisappear(formatter.format(nowDate));
    }
}
