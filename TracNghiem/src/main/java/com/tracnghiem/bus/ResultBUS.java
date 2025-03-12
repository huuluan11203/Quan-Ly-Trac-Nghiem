package com.tracnghiem.bus;

import com.tracnghiem.dao.ResultDAO;
import com.tracnghiem.dto.ResultDTO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultBUS {

    private final ResultDAO resultDAO = ResultDAO.getInstance();
    public static ArrayList<ResultDTO> listTest = new ArrayList<>();

    public ResultBUS() {
        listTest = resultDAO.selectAll();
    }

    public ArrayList<ResultDTO> getAll(){
        return resultDAO.selectAll();
    }
    
    public String convertMapToString(Map<Integer, Integer> userAnswers) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : userAnswers.entrySet()) {
            sb.append(entry.getKey()).append("-").append(entry.getValue()).append(";");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); // Xóa dấu `;` cuối cùng
        }
        return sb.toString();
    }

    public Map<Integer, Integer> convertStringToMap(String rsAnswers) {
        Map<Integer, Integer> userAnswers = new HashMap<>();
        if (rsAnswers == null || rsAnswers.isEmpty()) {
            return userAnswers;
        }
        String[] pairs = rsAnswers.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.split("-");
            if (keyValue.length == 2) {
                userAnswers.put(Integer.parseInt(keyValue[0]), Integer.parseInt(keyValue[1]));
            }
        }
        return userAnswers;
    }

    //Lưu kết quả bài thi
    public float submitExam(int userID, String exCode, String ex_questionIDs, Map<Integer, Integer> userAnswers) {
        Map<Integer, Integer> correctAnswers = getCorrectAnswers(ex_questionIDs);
        int correctCount = 0;

        // So sánh câu trả lời
        for (Map.Entry<Integer, Integer> entry : correctAnswers.entrySet()) {
            int questionID = entry.getKey();
            int correctAnswerID = entry.getValue();
            if (userAnswers.get(questionID) != null && userAnswers.get(questionID).equals(correctAnswerID)) {
                correctCount++;
            }
        }

        // Tính điểm theo thang 10
        float totalQuestions = correctAnswers.size();
        float score = (correctCount * 10.0f) / totalQuestions;
        ArrayList<Integer> answerList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : userAnswers.entrySet()) {
            answerList.add(entry.getKey());
            answerList.add(entry.getValue());
        }
        if (resultDAO.saveResult(userID, exCode, answerList, score)) {
            return score;
        }
        return 0;
    }
    
    
    public ResultDTO getResultByExCode(String exCode){
        return resultDAO.getResultByExCode(exCode);
    }
    
    public Map<Integer, Integer> getCorrectAnswers(String ex_questionIDs){
        return resultDAO.getCorrectAnswers(ex_questionIDs);
    }

}
