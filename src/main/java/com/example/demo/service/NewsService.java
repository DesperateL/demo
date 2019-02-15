package com.example.demo.service;


import com.example.demo.dao.NewsDAO;
import com.example.demo.model.News;
import com.example.demo.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

    public List<News> getAllNews(){
        return newsDAO.selectAll();
    }
    public String saveImage(MultipartFile file) throws IOException {
        int doPos = file.getOriginalFilename().lastIndexOf(".");
        if(doPos<0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(doPos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        //xxx.jpg
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;

    }
    public int addNews(News news){
        return newsDAO.addNews(news);
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public void updateCommentCount(int entityId, int count) {
        newsDAO.updateCommentCount(entityId,count);
    }

    public void updateLikeCount(int newsId, int likeCount) {
        newsDAO.updateLikeCount(likeCount,newsId);
    }
}
