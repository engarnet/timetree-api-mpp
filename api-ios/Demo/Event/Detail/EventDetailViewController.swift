//
//  EventDetailViewController.swift
//  Demo
//
//  Created by EngarNet on 2020/05/06.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import WebKit
import MapKit
import TimeTreeAPI

class EventDetailViewController: UIViewController {
    @IBOutlet weak var startTimeLabel: UILabel!
    @IBOutlet weak var endTimeLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var mapView: MKMapView!

    var calendar: TCalendar!
    var event: TEvent!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
    }

    private func setup() {
        self.navigationItem.rightBarButtonItems = [
            UIBarButtonItem(barButtonSystemItem: .trash, target: self, action: #selector(onDeleteTapped(_:))),
            UIBarButtonItem(barButtonSystemItem: .edit, target: self, action: #selector(onEditTapped(_:)))
        ]

        self.title = event.title
        self.descriptionLabel.text = event.description
        self.startTimeLabel.text = event.startAt.displayFormat
        self.endTimeLabel.text = event.endAt.displayFormat
        if let url = event.url {
            self.webView.load(URLRequest(url: url))
        }
        if let location = event.location {
            location.coordinateRegion {
                self.mapView.setRegion($0, animated: false)
            }
        }
    }
    
    @objc func onEditTapped(_ sender: UIView) {
        let vc = EventEditViewController.instance(calendar: calendar, event: event)
        navigationController?.pushViewController(vc, animated: true)
    }

    @objc func onDeleteTapped(_ sender: UIView) {
        Container.timeTreeClient.deleteEvent(
        calendarId: calendar.id, eventId: event.id) { result in
            switch result {
            case .success(_):
                break
            case .failure(let error):
                print(error)
            }
            DispatchQueue.main.async {
                let nc = self.navigationController!
                let vc = nc.viewControllers[nc.viewControllers.count - 2] as? EventListViewController
                vc?.fetchEvents()
                nc.popViewController(animated: true)
            }
        }
    }
}

private extension String {
    func coordinateRegion(completion: @escaping (MKCoordinateRegion) -> ()) {
        let geocoder = CLGeocoder()
        geocoder.geocodeAddressString(
            self,
            completionHandler: { (places, error) in
                if let error = error {
                    print(error)
                } else {
                    if let place = places?.first {
                        if let coordinate = place.location?.coordinate {
                            let region = MKCoordinateRegion(
                                center: coordinate,
                                span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
                            )
                            completion(region)
                        }
                    }
                }
            }
        )
    }
}

private extension Date {
    var displayFormat: String {
        get {
            let dateFormatter = DateFormatter()
            dateFormatter.dateStyle = .long
            dateFormatter.timeStyle = .none
            let timeFormatter = DateFormatter()
            timeFormatter.dateStyle = .none
            timeFormatter.timeStyle = .short
            let date = dateFormatter.string(from: self)
            let time = timeFormatter.string(from: self)
            return date + "\n" + time
        }
    }
}

extension EventDetailViewController {
    static func instance(calendar: TCalendar, event: TEvent) -> EventDetailViewController {
        let sb = UIStoryboard(name: "EventDetail", bundle: nil)
        let vc = sb.instantiateInitialViewController() as! EventDetailViewController
        vc.calendar = calendar
        vc.event = event
        return vc
    }
}
