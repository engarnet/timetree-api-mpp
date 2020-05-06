//
//  UINavigationController.swift
//  Demo
//
//  Created by EngarNet on 2020/05/07.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit

extension UINavigationController {
    func popToViewController(ofClass: AnyClass, animated: Bool = true) {
        if let vc = viewControllers.filter({$0.isKind(of: ofClass)}).last {
            popToViewController(vc, animated: animated)
        }
    }

    func findViewController(ofClass: AnyClass) -> UIViewController? {
        return viewControllers.filter({$0.isKind(of: ofClass)}).last
    }
}
