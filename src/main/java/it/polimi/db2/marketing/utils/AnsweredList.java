package it.polimi.db2.marketing.utils;

import it.polimi.db2.marketing.ejb.entities.Question;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnsweredList {
    private final Map<Integer, String> questionIdMap = new HashMap<>();
    private final Map<String, Map<Integer, String>> userAnswerMap = new HashMap<>();

    public AnsweredList(List<Question> questions) {
        for (Question q : questions) {
            questionIdMap.put(q.getId(), q.getText());
        }
    }

    public void addAnswer(String userName, int questionId, String answer) {
        Map<Integer, String> qa = new HashMap<>();
        qa.put(questionId, answer);
        userAnswerMap.put(userName, qa);
    }

    public Collection<String> getAllUsers() {
        return userAnswerMap.keySet();
    }

    public Collection<Integer> getAllQuestionId() {
        return questionIdMap.keySet();
    }

    public String getQuestionText(int id) {
        return questionIdMap.get(id);
    }

    public int getQuestionSize() {
        return questionIdMap.size();
    }

    public String getAnswer(String userName, int questionId) {
        return userAnswerMap.get(userName).get(questionId);
    }
}
