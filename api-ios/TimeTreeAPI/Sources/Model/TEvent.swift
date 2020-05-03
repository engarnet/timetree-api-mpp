//
//  TEvent.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/02.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPICommon

public struct TEvent{
    public let id: String
    public let isKeep: Bool
    public let title: String
    public let allDay: Bool
    public let startAt: Foundation.Date
    public let startTimezone: TimeZone
    public let endAt: Foundation.Date
    public let endTimezone: TimeZone
    public let recurrence: [String]?
    public let recurringUuid: String?
    public let description: String?
    public let location: String?
    public let url: URL?
    public let creator: TUser
    public let label: TLabel
    public let attendees: [TUser]
    public let createdAt: Foundation.Date
    public let updatedAt: Foundation.Date
}

extension EventResponse {
    func toModel() -> TEvent {
        let entity = self.data
        return TEvent(
            id: entity.id,
            isKeep: entity.attributes.category == Category.keep,
            title: entity.attributes.title,
            allDay: entity.attributes.allDay,
            startAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.startAt.time)),
            startTimezone: TimeZone(identifier: entity.attributes.startTimezone)!,
            endAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.endAt.time)),
            endTimezone: TimeZone(identifier: entity.attributes.endTimezone)!,
            recurrence: entity.attributes.recurrence?.map{_ in ""},
            recurringUuid: entity.attributes.recurringUuid,
            description: entity.attributes.description,
            location: entity.attributes.location,
            url: URL(string: entity.attributes.url ?? ""),
            creator: included.first{
                $0.id == entity.relationships.creator.data.id
            }.map {
                TUser(
                    id: $0.id,
                    name: $0.attributes.name,
                    description: $0.attributes.component3(),
                    imageUrl: URL(string: $0.attributes.imageUrl ?? "")
                )
                } ?? TUser(id: entity.relationships.creator.data.id, name: "workaround name", description: "", imageUrl: nil), // TODO: includedにmembersが返って来ない場合の回避策
            label: included.first{
                $0.id == entity.relationships.label.data.id
            }.map {
                TLabel(
                    id: $0.id,
                    name: $0.attributes.name,
                    color: UIColor(rgb: $0.attributes.color as! Int)
                )
                } ?? TLabel(id: entity.relationships.label.data.id, name: "workaround name", color: UIColor.white), // TODO: includedにmembersが返って来ない場合の回避策
            attendees: entity.relationships.attendees.data.map{ data in
                data.id
            }.map { attendeesId in
                included.first { $0.id == attendeesId }
                .map {
                    TUser(
                        id: $0.id,
                        name: $0.attributes.name,
                        description: $0.attributes.component3(),
                        imageUrl: URL(string: $0.attributes.imageUrl ?? "")
                    )
                    } ?? TUser(id: attendeesId, name: "workaround name", description: "", imageUrl: nil) // TODO: includedにmembersが返って来ない場合の回避策
            },
            createdAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.createdAt.time)),
            updatedAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.updatedAt.time))
        )
    }
}

extension UpcomingEventsResponse {
    func toModel() -> [TEvent] {
        return data.map { entity in
            TEvent(
                id: entity.id,
                isKeep: entity.attributes.category == Category.keep,
                title: entity.attributes.title,
                allDay: entity.attributes.allDay,
                startAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.startAt.time)),
                startTimezone: TimeZone(identifier: entity.attributes.startTimezone)!,
                endAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.endAt.time)),
                endTimezone: TimeZone(identifier: entity.attributes.endTimezone)!,
                recurrence: entity.attributes.recurrence?.map{_ in ""},
                recurringUuid: entity.attributes.recurringUuid,
                description: entity.attributes.description,
                location: entity.attributes.location,
                url: URL(string: entity.attributes.url ?? ""),
                creator: included.first{
                    $0.id == entity.relationships.creator.data.id
                }.map {
                    TUser(
                        id: $0.id,
                        name: $0.attributes.name,
                        description: $0.attributes.component3(),
                        imageUrl: URL(string: $0.attributes.imageUrl ?? "")
                    )
                    } ?? TUser(id: entity.relationships.creator.data.id, name: "workaround name", description: "", imageUrl: nil), // TODO: includedにmembersが返って来ない場合の回避策
                label: included.first{
                    $0.id == entity.relationships.label.data.id
                }.map {
                    TLabel(
                        id: $0.id,
                        name: $0.attributes.name,
                        color: UIColor(rgb: $0.attributes.color as! Int)
                    )
                    } ?? TLabel(id: entity.relationships.label.data.id, name: "workaround name", color: UIColor.white), // TODO: includedにmembersが返って来ない場合の回避策
                attendees: entity.relationships.attendees.data.map{ data in
                    data.id
                }.map { attendeesId in
                    included.first { $0.id == attendeesId }
                    .map {
                        TUser(
                            id: $0.id,
                            name: $0.attributes.name,
                            description: $0.attributes.component3(),
                            imageUrl: URL(string: $0.attributes.imageUrl ?? "")
                        )
                        } ?? TUser(id: attendeesId, name: "workaround name", description: "", imageUrl: nil) // TODO: includedにmembersが返って来ない場合の回避策
                },
                createdAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.createdAt.time)),
                updatedAt: Foundation.Date(timeIntervalSince1970: TimeInterval(entity.attributes.updatedAt.time))
            )
        }
    }
}
