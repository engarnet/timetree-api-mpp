//
//  TError.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import Foundation
import TimeTreeAPICommon

public struct TError {
    public let responseCode: Int
    public let type: String?
    public let status: Int?
    public let title: String?
    public let errors: Dictionary<String, [String]>?
    public let error: String?
    public let errorDescription: String?
}

extension ErrorEntity {
    func toModel(responseCode: Int) -> TError {
        return TError(
            responseCode: responseCode,
            type: self.type,
            status: self.status as? Int,
            title: self.title,
            errors: self.errors,
            error: self.error,
            errorDescription: self.errorDescription
        )
    }
}
