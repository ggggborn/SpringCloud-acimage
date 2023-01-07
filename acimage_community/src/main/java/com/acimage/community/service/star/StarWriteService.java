package com.acimage.community.service.star;


public interface StarWriteService {


    void saveStar(long userId,long topicId);

    void removeStar(long userId,long topicId);

    void removeStars(long topicId);


}
