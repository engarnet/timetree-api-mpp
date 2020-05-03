//
//  TimeTreeError.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/03.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import Foundation

public enum TimeTreeError: Error {
    case BadRequestError(TError)
    case UnauthorizedError(TError)
    case ForbiddenError(TError)
    case NotFoundError(TError)
    case NotAcceptableError(TError)
    case TooManyRequestError(TError)
    case InternalServerErrorError(TError)
    case ServiceUnavailableError(TError)
    case TimeoutError(TError)
    case UnknownError(String)
}
