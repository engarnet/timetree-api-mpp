//
//  TCalendar.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPICommon

public class TCalendar{
    public init(
        id: String,
        name: String,
        description: String,
        color: UIColor,
        order: Int,
        imageUrl: URL?,
        createdAt: Foundation.Date,
        labels: [TLabel],
        members: [TUser]
    ) {
        self.id = id
        self.name = name
        self.description = description
        self.color = color
        self.order = order
        self.imageUrl = imageUrl
        self.createdAt = createdAt
        self.labels = labels
        self.members = members
    }

    public let id: String
    public let name: String
    public let description: String
    public let color: UIColor
    public let order: Int
    public let imageUrl: URL?
    public let createdAt: Foundation.Date
    public let labels: [TLabel]
    public let members: [TUser]
}

extension CalendarResponse {
    func toModel() -> TCalendar {
        let entity = self.data
        return TCalendar(
            id: entity.id,
            name: entity.attributes.name,
            description: entity.attributes.component2(),
            color: UIColor(rgb: Int(entity.attributes.color)),
            order: Int(entity.attributes.order),
            imageUrl: URL(string: entity.attributes.imageUrl ?? ""),
            createdAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.createdAt.time)),
            labels: entity.relationships.labels.data.map{ dataEntity in
                dataEntity.id
            }.map{ id in
                (included.first { $0.id == id }!)
            }.map{ data in
                TLabel(
                    id: data.id,
                    name: data.attributes.name,
                    color: UIColor(rgb: data.attributes.color as! Int)
                )
            },
            members: entity.relationships.members.data.map{ dataEntity in
                dataEntity.id
            }.map{ id in
                (included.first { $0.id == id }!)
            }.map{ data in
                TUser(
                    id: data.id,
                    name: data.attributes.name,
                    description: data.attributes.component3(),
                    imageUrl: URL(string: data.attributes.imageUrl ?? "")
                )
            }
        )
    }
}

extension CalendarsResponse {
    func toModel() -> [TCalendar] {
        data.map { entity in
            TCalendar(
                id: entity.id,
                name: entity.attributes.name,
                description: entity.attributes.component2(),
                color: UIColor(rgb: Int(entity.attributes.color)),
                order: Int(entity.attributes.order),
                imageUrl: URL(string: entity.attributes.imageUrl ?? ""),
                createdAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.createdAt.time)),
                labels: entity.relationships.labels.data.map{ dataEntity in
                    dataEntity.id
                }.map{ id in
                    (included.first { $0.id == id }!)
                }.map{ data in
                    TLabel(
                        id: data.id,
                        name: data.attributes.name,
                        color: UIColor(rgb: data.attributes.color as! Int)
                    )
                },
                members: entity.relationships.members.data.map{ dataEntity in
                    dataEntity.id
                }.map{ id in
                    (included.first { $0.id == id }!)
                }.map{ data in
                    TUser(
                        id: data.id,
                        name: data.attributes.name,
                        description: data.attributes.component3(),
                        imageUrl: URL(string: data.attributes.imageUrl ?? "")
                    )
                }
            )
        }
    }
}
