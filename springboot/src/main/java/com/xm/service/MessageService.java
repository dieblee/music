package com.xm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.entity.Message;
import com.xm.entity.Song;
import com.xm.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service//标志为spingboot中的容器
public class MessageService {
    @Resource
    private MessageMapper messageMapper;

    public List<Message> selectAll(Message message) {
        List<Message> list = messageMapper.selectAll(message);
        return list;
    }

    public List<Message> selectByReceiveId(Integer id) {return messageMapper.selectByReceiveId(id);
    }
    public List<Message> selectBySendId(Integer id) {return messageMapper.selectBySendId(id);
    }


    public PageInfo<Message> selectPage(Message message, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Message> list = messageMapper.selectAll(message);
        return PageInfo.of(list);
    }
    public void update(Message message) {
        messageMapper.updateById(message);
    }
    public void deleteById(Integer id) {
        messageMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
    public void add(Message message) {
        messageMapper.insert(message);
    }

}
