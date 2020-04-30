//
//  TLabel.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import Foundation
import UIKit
import TimeTreeAPICommon

public struct TLabel{
    public let id: String
    public let name: String
    public let color: UIColor
}

extension CalendarLabelsResponse {
    func toModel() -> [TLabel] {
        data.map { label in
            TLabel(
                id: label.id,
                name: label.attributes.name,
                color: UIColor(rgb: Int(label.attributes.color))
            )
        }
    }
}
