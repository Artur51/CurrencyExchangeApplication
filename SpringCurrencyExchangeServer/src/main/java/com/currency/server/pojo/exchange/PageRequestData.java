package com.currency.server.pojo.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestData {
    int pageSize;
    int pageNumber;
    int offset;
}
