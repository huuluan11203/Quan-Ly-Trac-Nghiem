
package com.tracnghiem.bus;

import com.tracnghiem.dao.LogDAO;
import com.tracnghiem.dto.LogDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author huulu
 */
public class LogBUS {
    private LogDAO logDAO;

    public LogBUS() {
        this.logDAO = LogDAO.getInstance();
    }

    // Thêm log mới
    public boolean saveLog(String content, int userID, String exCode) {
        LogDTO log = new LogDTO();
        log.setLogContent(content);
        log.setLogUserID(userID);
        log.setLogExCode(exCode);
        log.setLogDate(LocalDateTime.now()); // Thời gian hiện tại
        return logDAO.insert(log);
    }

    // Cập nhật log
    public boolean updateLog(int logID, String content, int userID, String exCode) {
        LogDTO log = logDAO.selectByID(String.valueOf(logID));
        if (log != null) {
            log.setLogContent(content);
            log.setLogUserID(userID);
            log.setLogExCode(exCode);
            log.setLogDate(LocalDateTime.now()); // Cập nhật thời gian mới
            return logDAO.update(log);
        }
        return false;
    }

    // Xóa log theo ID
    public boolean deleteLog(int logID) {
        return logDAO.delete(String.valueOf(logID));
    }

    // Lấy danh sách tất cả log
    public ArrayList<LogDTO> getAllLogs() {
        return logDAO.selectAll();
    }

    // Lấy log theo ID
    public LogDTO getLogByID(int logID) {
        return logDAO.selectByID(String.valueOf(logID));
    }

    // Tìm kiếm log theo nội dung
    public ArrayList<LogDTO> searchLogsByContent(String keyword) {
        ArrayList<LogDTO> allLogs = logDAO.selectAll();
        ArrayList<LogDTO> result = new ArrayList<>();
        for (LogDTO log : allLogs) {
            if (log.getLogContent().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(log);
            }
        }
        return result;
    }
}
