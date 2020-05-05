//
//  ViewController.swift
//  Demo
//
//  Created by EngarNet on 2020/05/01.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPI

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.

        let client = TimeTreeClient(accessToken: "")
        client.user { result in
            switch result {
            case .success(let user):
                print(user.name)
            case .failure(let error):
                print(error)
            }
        }

        client.calendars { result in
            switch result {
            case .success(let calendars):
                calendars.forEach {
                    print($0.name)
                    client.upcomingEvents(calendarId: $0.id, timeZone: TimeZone.current) { events in
                        switch events {
                        case .success(let events):
                            events.forEach { print($0.title) }
                        case .failure(let error):
                            print(error)
                        }
                    }
                }
            case .failure(let error):
                print(error)
            }
        }
        
        client.calendarLabels(calendarId: "hGL8r83T18UE") { result in
            switch result {
            case .success(let labels):
                client.addSchedule(
                    calendarId: "hGL8r83T18UE",
                    title: "ios_sche",
                    allDay: false,
                    startAt: Date(),
                    endAt: Date(),
                    description: "説明",
                    location: "大阪",
                    url: URL(string: "http://www.yahoo.co.jp"),
                    label: labels[0],
                    attendees: nil
                ) {result in
                    switch result {
                    case .success(let event):
                        print(event.id)
                    case .failure(let error):
                        print(error)
                    }
                }
            case .failure(let error):
                print(error)
            }
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        let vc = AuthorizeViewController()
        vc.delegate = self
        vc.params = AuthorizeParams(
            clientId: "qkEtBbKozZFwOUdl3SMcq2A2RbNwgFZTIvgf0UJ648U",
            clientSecret: "iFYXwc0Zew0yJPQ6oHUDnqR6hOBousbITfLXRoCo1Gc",
            redirectUrl: "https://sites.google.com/engar-net.com/timetreetest",
            state: UUID().uuidString,
            codeVerifier: UUID().uuidString
        )
        self.present(vc, animated: true, completion: nil)
    }
}

extension ViewController: AuthorizeViewControllerDelegate {
    func authorizeCompleted(accessToken: TAccessToken) {
        print("token=", accessToken.accessToken)
        let client = TimeTreeClient(accessToken: accessToken.accessToken)
        client.user { result in
            switch result {
            case .success(let user):
                print(user.name)
            case .failure(let error):
                print(error)
            }
        }
    }
}
