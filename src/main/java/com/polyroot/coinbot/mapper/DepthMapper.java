package com.polyroot.coinbot.mapper;

import com.polyroot.coinbot.model.document.Depth;
import com.polyroot.coinbot.model.dto.DepthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DepthMapper {

    @Mapping(target = "id", ignore = true)
    Depth depthResponseToDepth(DepthResponse depthResponse);

}
