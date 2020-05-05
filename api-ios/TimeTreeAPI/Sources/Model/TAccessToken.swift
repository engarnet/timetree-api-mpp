//
//  TAccessToken.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import Foundation
import TimeTreeAPICommon

public struct TAccessToken {
    public init(
        accessToken: String,
        tokenType: String,
        scope: String,
        createdAt: Foundation.Date
    ) {
        self.accessToken = accessToken
        self.tokenType = tokenType
        self.scope = scope
        self.createdAt = createdAt
    }
    
    public let accessToken: String
    public let tokenType: String
    public let scope: String
    public let createdAt: Foundation.Date
}

extension TokenResponse {
    func toModel() -> TAccessToken {
        return TAccessToken(
            accessToken: self.accessToken,
            tokenType: self.tokenType,
            scope: self.scope,
            createdAt: Foundation.Date(timeIntervalSince1970: TimeInterval(self.createdAt))
        )
    }
}
