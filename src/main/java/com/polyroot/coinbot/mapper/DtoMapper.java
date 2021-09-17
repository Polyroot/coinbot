package com.polyroot.coinbot.mapper;

import com.polyroot.coinbot.model.document.*;
import com.polyroot.coinbot.model.dto.*;
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

    @Mapping(target = "id", ignore = true)
    Trade tradeResponseToTrade(TradeResponse tradeResponse);

    @Mapping(target = "id", ignore = true)
    KLine kLineResponseToKLine(KLineResponse tradeResponse);

    @Mapping(target = "id", ignore = true)
    MiniTicker miniTickerResponseToMiniTicker(MiniTickerResponse miniTickerResponse);

    @Mapping(target = "id", ignore = true)
    Ticker tickerResponseToTicker(TickerResponse tickerResponse);

    @Mapping(target = "id", ignore = true)
    BookTicker bookTickerResponseToBookTicker(BookTickerResponse bookTickerResponse);

    @Mapping(target = "id", ignore = true)
    MarkPrice markPriceResponseToMarkPrice(MarkPriceResponse markPriceResponse);

    @Mapping(target = "id", ignore = true)
    ForceOrder forceOrderResponseToForceOrder(ForceOrderResponse forceOrderResponse);
}
