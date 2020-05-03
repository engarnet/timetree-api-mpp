//
//  TUser.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import Foundation
import TimeTreeAPICommon

public struct TUser {
    public let id: String
    public let name: String
    public let description: String
    public let imageUrl: URL?
}

extension UserResponse {
    func toModel() -> TUser {
        return TUser(
            id: data.id,
            name: data.attributes.name,
            description: data.attributes.component2(),
            imageUrl: URL(string: data.attributes.imageUrl ?? "")
        )
    }
}

extension CalendarMembersResponse {
    func toModel() -> [TUser] {
        data.map {
            TUser(
                id: $0.id,
                name: $0.attributes.name,
                description: $0.attributes.component2(),
                imageUrl: URL(string: $0.attributes.imageUrl ?? "")
            )
        }
    }
}
