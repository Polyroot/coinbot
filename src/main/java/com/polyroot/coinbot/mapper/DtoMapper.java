package com.polyroot.coinbot.mapper;

import com.polyroot.coinbot.model.document.AggTrade;
import com.polyroot.coinbot.model.document.Depth;
import com.polyroot.coinbot.model.document.MarketSocketRequest;
import com.polyroot.coinbot.model.document.MarketSocketResponse;
import com.polyroot.coinbot.model.dto.AggTradeResponse;
import com.polyroot.coinbot.model.dto.DepthResponse;
import com.polyroot.coinbot.model.dto.MarketSocketRequestDto;
import com.polyroot.coinbot.model.dto.MarketSocketResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface DtoMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target="requestId", source="marketSocketRequestDto.id")
    })
    MarketSocketRequest marketSocketRequestDtoToMarketSocketRequest(MarketSocketRequestDto marketSocketRequestDto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target="requestId", source="marketSocketResponseDto.id")
    })
    MarketSocketResponse marketSocketResponseDtoToMarketSocketResponse(MarketSocketResponseDto marketSocketResponseDto);

    @Mapping(target = "id", ignore = true)
    Depth depthResponseToDepth(DepthResponse depthResponse);

    @Mapping(target = "id", ignore = true)
    AggTrade aggTradeResponseToAggTrade(AggTradeResponse aggTradeResponse);

}
