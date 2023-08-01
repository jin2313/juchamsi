package com.inet.juchamsi.domain.chat.application.impl;

import com.inet.juchamsi.domain.chat.application.ChatService;
import com.inet.juchamsi.domain.chat.dao.ChatPeopleRepository;
import com.inet.juchamsi.domain.chat.dao.ChatRoomRepository;
import com.inet.juchamsi.domain.chat.dto.request.SystemChatRoomRequest;
import com.inet.juchamsi.domain.chat.dto.response.ChatRoomResponse;
import com.inet.juchamsi.domain.chat.entity.ChatPeople;
import com.inet.juchamsi.domain.chat.entity.ChatRoom;
import com.inet.juchamsi.domain.chat.entity.Type;
import com.inet.juchamsi.domain.user.dao.UserRepository;
import com.inet.juchamsi.domain.user.entity.User;
import com.inet.juchamsi.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.inet.juchamsi.domain.chat.entity.Status.ALIVE;
import static com.inet.juchamsi.domain.chat.entity.Type.GENERAL;
import static com.inet.juchamsi.domain.chat.entity.Type.SYSTEM;
import static com.inet.juchamsi.global.common.Active.ACTIVE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatPeopleRepository chatPeopleRepository;
    private final UserRepository userRepository;
    
    // 채팅방 불러오기
    @Override
    public List<ChatRoomResponse> showChatRoom(String loginId) {
        List<ChatRoom> results = chatRoomRepository.findAllRoomsByLoginIdAndStatus(loginId, ACTIVE, ALIVE);
        List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();
        for (ChatRoom result : results) {
            chatRoomResponses.add(ChatRoomResponse.builder()
                            .roomId(result.getRoomId())
                            .roomName(result.getRoomName())
                            .build());
        }
        return chatRoomResponses;
    }
    
    // 채팅방 하나 불러오기
    @Override
    public ChatRoomResponse showDetailChatRoom(String roomId) {
        Optional<ChatRoom> result = chatRoomRepository.findChatRoomByIdAndLoginIdAndStatus(roomId, ALIVE);
        if (result.isEmpty()) {
            throw new NotFoundException(ChatRoom.class, roomId);
        }
        
        return ChatRoomResponse.builder()
                .roomId(result.get().getRoomId())
                .roomName(result.get().getRoomName())
                .build();
    }

    /* 유저간 채팅방 */
    // 채팅방 생성
    @Override
    public ChatRoomResponse createRoom(String name) {
        ChatRoom room = chatRoomRepository.save(ChatRoom.create(name, GENERAL));
        return ChatRoomResponse.builder() 
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .build();
    }

    /* 시스템 채팅방 */
    // 시스템 채팅방 생성
    @Override
    public ChatRoomResponse createSystemRoom(SystemChatRoomRequest request) {
        String userId = request.getUserId();
        String name = "주참시";
        // chatRoom 생성
        ChatRoom room = chatRoomRepository.save(ChatRoom.create(name, SYSTEM));
        // userId로 user 정보 가져오기
        Optional<User> user = userRepository.findByLoginId(userId);
        // user 정보 없으면 -> NotFoundException 발생
        if (user.isEmpty()) {
            throw new NotFoundException(User.class, userId);
        }

        // chatPeople -> user정보(userId) 넣기
        ChatPeople people = chatPeopleRepository.save(ChatPeople.builder()
                        .chatRoom(room)
                        .user(user.get())
                        .build());

        return ChatRoomResponse.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .build();
    }
}
