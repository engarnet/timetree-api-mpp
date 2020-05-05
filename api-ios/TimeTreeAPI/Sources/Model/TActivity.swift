//
//  TActivity.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import Foundation
import TimeTreeAPICommon

public class TActivity {
    public init(
        id: String,
        content: String,
        updatedAt: Foundation.Date,
        createdAt: Foundation.Date
    ) {
        self.id = id
        self.content = content
        self.updatedAt = updatedAt
        self.createdAt = createdAt
    }

    public let id: String
    public let content: String
    public let updatedAt: Foundation.Date
    public let createdAt: Foundation.Date
}

extension ActivityResponse {
    func toModel() -> TActivity {
        return TActivity(
            id: self.data.id,
            content: self.data.attributes.content,
            updatedAt: Foundation.Date(timeIntervalSince1970: TimeInterval(self.data.attributes.updatedAt.time)),
            createdAt: Foundation.Date(timeIntervalSince1970: TimeInterval(self.data.attributes.createdAt.time))
        )
    }
}
