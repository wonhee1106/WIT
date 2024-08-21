package com.wit.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wit.services.ChatRoomService;
import com.wit.services.ChatService;

@Controller
@RequestMapping("/chatroom/")
public class ChatRoomController {
    @Autowired
    private ChatRoomService serv;

    @Autowired
    private ChatService chatServ;

    @Autowired
    private HttpSession session;

    // 개인 채팅방 생성
    @RequestMapping("create")
    @ResponseBody
    public String createPrivateChat(String emp_no1) {
        String empNo2 = (String) session.getAttribute("loginID");
        String result = serv.createPrivateChat(emp_no1, empNo2);
        if (result.equals("success")) {
            return "success";
        }
        return "false";
    }

    // 그룹 채팅방 생성
    @RequestMapping("createGroup")
    @ResponseBody
    public String createGroupChat(String chatRoomName, @RequestParam("empNos") List<String> empNos) {
        String empNo = (String) session.getAttribute("loginID");
        serv.createGroupChat(chatRoomName, empNos, empNo);
        return "success";
    }

    // 채팅방 목록 조회
    @RequestMapping("myChatRooms")
    @ResponseBody
    public List<Map<String, Object>> getMyChatRooms() {
        String empNo = (String) session.getAttribute("loginID");
        return serv.getChatRoomsByUserId(empNo);
    }

    // 채팅방 상세 조회
    @RequestMapping("details")
    @ResponseBody
    public Map<String, Object> getDetailChatRooms(int chat_room_seq) {
    	String empNo = (String) session.getAttribute("loginID");
        return serv.getDetailChatRooms(chat_room_seq, empNo);
    }

    // 채팅방 제목 수정
    @RequestMapping("updateTitle")
    @ResponseBody
    public String updateChatRoomTitle(int chat_room_seq, String new_title) {
    	String empNo = (String) session.getAttribute("loginID");
        serv.updateChatRoomTitle(chat_room_seq, new_title, empNo);
        return "Title updated";
    }

    // 채팅방 나가기
    @RequestMapping("exit")
    @ResponseBody
    public String exitChatRoom(int chat_room_seq) {
        String empNo = (String) session.getAttribute("loginID");
        String result = serv.exitChatRoom(chat_room_seq, empNo);
        return result;
    }
    
    @RequestMapping("updateReadCount")
    @ResponseBody
    public Map<String, Object> updateReadCount(
        String chatRoomSeq, String messageSeq) {
        String userName = (String) session.getAttribute("loginID");
        Map<String, Object> result = new HashMap<>();
        try {
            // 사용자가 이미 읽었는지 확인
            boolean isAlreadyRead = chatServ.markMessagesAsRead(chatRoomSeq, Integer.parseInt(messageSeq), userName);
            if (!isAlreadyRead) {
                int updatedReadCount = chatServ.decreaseReadCount(chatRoomSeq, Integer.parseInt(messageSeq), userName);
                result.put("status", "success");
                result.put("updated_read_count", updatedReadCount);
            } else {
                result.put("status", "already_read");
                result.put("updated_read_count", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
        }
        return result;
    }
    
    // 예외를 담당하는 메서드 생성
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        e.printStackTrace();
        return "error";
    }
}
