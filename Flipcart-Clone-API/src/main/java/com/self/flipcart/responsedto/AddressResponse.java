package com.self.flipcart.responsedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private String addressId;
    private String addressLine1;
    private String addressLine2;
    private String areaVillage;
    private String cityDistrict;
    private String state;
    private String Country;
    private int pincode;
}
