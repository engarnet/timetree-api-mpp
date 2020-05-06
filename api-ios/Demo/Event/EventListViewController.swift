//
//  EventListViewController.swift
//  Demo
//
//  Created by EngarNet on 2020/05/06.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPI

class EventListViewController: UITableViewController {
    var calendar: TCalendar!
    private var events: [TEvent] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Upcoming Event List"

        fetchEvents()

        self.navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(onAddTapped(_:)))
    }
    
    func fetchEvents() {
        Container.timeTreeClient.upcomingEvents(calendarId: calendar.id, timeZone: TimeZone.current) { result in
            switch result {
            case .success(let events):
                self.reloadData(events: events)
            case .failure(let error):
                print(error)
            }
        }
    }
    
    @objc func onAddTapped(_ sender: UIView) {
        let vc = EventEditViewController.instance(calendar: calendar)
        navigationController?.pushViewController(vc, animated: true)
    }

    private func reloadData(events: [TEvent]) {
        removeAllCells()
        self.events = events
        tableView.reloadData()
    }

    private func removeAllCells() {
        self.events = []
        let count = self.tableView.numberOfRows(inSection: 0)
        let deleteItems = (0..<count).map { index in
            IndexPath(row: index, section: 0)
        }
        self.tableView.deleteRows(at: deleteItems, with: .automatic)
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        events.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "EventListCell") as? EventListCell else {
            return UITableViewCell()
        }
        let event = events[indexPath.row]
        cell.colorView.backgroundColor = UIColor.gray
        cell.nameLabel.text = event.title
        cell.descriptionLabel.text = event.description
        cell.timeLabel.text = event.startAt.displayFormat + " - " + event.endAt.displayFormat
        return cell
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        moveEventDetail(event: self.events[indexPath.row])
    }
    
    private func moveEventDetail(event: TEvent) {
        let vc = EventDetailViewController.instance(calendar: self.calendar, event: event)
        self.navigationController?.pushViewController(vc, animated: true)
    }
}

private extension Date {
    var displayFormat: String {
        get {
            let formatter = DateFormatter()
            formatter.dateStyle = .short
            formatter.timeStyle = .short
            return formatter.string(from: self)
        }
    }
}

extension EventListViewController {
    static func instance(calendar: TCalendar) -> EventListViewController {
        let sb = UIStoryboard(name: "EventList", bundle: nil)
        let vc = sb.instantiateInitialViewController() as! EventListViewController
        vc.calendar = calendar
        return vc
    }
}

class EventListCell: UITableViewCell {
    @IBOutlet weak var colorView: UIView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var timeLabel: UILabel!
}
