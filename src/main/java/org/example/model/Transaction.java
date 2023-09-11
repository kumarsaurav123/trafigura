package org.example.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Transaction {
 private Integer transactionID;
    private Integer tradeID;
    private Integer version;
    private String securityCode;
    private Integer quantity;
    private Type type;
    private BuySell buySell;

}
