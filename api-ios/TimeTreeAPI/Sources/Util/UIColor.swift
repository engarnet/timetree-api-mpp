//
//  UIColor.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit

extension UIColor {
    convenience init(rgb: Int) {
        let r = CGFloat((rgb & 0xFF0000) >> 16) / 255.0
        let g = CGFloat((rgb & 0x00FF00) >>  8) / 255.0
        let b = CGFloat( rgb & 0x0000FF       ) / 255.0
        self.init(red: r, green: g, blue: b, alpha: 1.0)
    }
}
