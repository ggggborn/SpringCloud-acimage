package com.acimage.admin.model.request;

import com.acimage.common.model.domain.community.HomeCarousel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CarouselModifyReq {
    @NotNull
    @Positive
    private Integer id;
    @Size(min = HomeCarousel.DESC_MIN, max = HomeCarousel.DESC_MAX, message = HomeCarousel.DESC_INVALID_MSG)
    String description;
    @NotNull
    @Size(min = 0, max = HomeCarousel.LINK_MAX, message = HomeCarousel.LINK_INVALID_MSG)
    String link;

}
