
print("FOOBAR!");

conn = new Mongo("cslvm31:27017");
db = conn.getDB("test");

doc = [
  {
    "game": 1,
    "action": {
      "actionType": "gameStart",
      "actionNumber": 1
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "gameStart",
      "actionNumber": 1
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 7,
      "actionNumber": 2,
      "location": {
        "x": 16,
        "y": 12
      },
      "points": 7
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": -5,
      "actionNumber": 3,
      "location": {
        "x": 6,
        "y": 12
      },
      "points": 2
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 6,
      "actionNumber": 2,
      "location": {
        "x": 5,
        "y": 12
      },
      "points": 6
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 8,
      "actionNumber": 4,
      "location": {
        "x": 12,
        "y": 9
      },
      "points": 10
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 4,
      "actionNumber": 5,
      "location": {
        "x": 15,
        "y": 13
      },
      "points": 14
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 14,
      "actionNumber": 6,
      "location": {
        "x": 13,
        "y": 2
      },
      "points": 28
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 6,
      "actionNumber": 7,
      "location": {
        "x": 12,
        "y": 15
      },
      "points": 34
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 7,
      "actionNumber": 8,
      "location": {
        "x": 6,
        "y": 6
      },
      "points": 41
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 6,
      "actionNumber": 3,
      "location": {
        "x": 17,
        "y": 1
      },
      "points": 12
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 2,
      "actionNumber": 9,
      "location": {
        "x": 9,
        "y": 8
      },
      "points": 43
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "SpecialMove",
      "pointsAdded": 13,
      "move": "Invert",
      "actionNumber": 4,
      "points": 25
    },
    "user": "u4285"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 4,
      "actionNumber": 5,
      "location": {
        "x": 9,
        "y": 5
      },
      "points": 29
    },
    "user": "u4285"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 5,
      "actionNumber": 6,
      "location": {
        "x": 3,
        "y": 11
      },
      "points": 34
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 12,
      "actionNumber": 10,
      "location": {
        "x": 4,
        "y": 9
      },
      "points": 55
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 4,
      "actionNumber": 11,
      "location": {
        "x": 5,
        "y": 9
      },
      "points": 59
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": -5,
      "actionNumber": 7,
      "location": {
        "x": 11,
        "y": 12
      },
      "points": 29
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 12,
      "actionNumber": 12,
      "location": {
        "x": 10,
        "y": 4
      },
      "points": 71
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 8,
      "actionNumber": 13,
      "location": {
        "x": 8,
        "y": 1
      },
      "points": 79
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 6,
      "actionNumber": 8,
      "location": {
        "x": 11,
        "y": 3
      },
      "points": 35
    },
    "user": "u4285"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 0,
      "actionNumber": 9,
      "location": {
        "x": 19,
        "y": 3
      },
      "points": 35
    },
    "user": "u4285"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 12,
      "actionNumber": 10,
      "location": {
        "x": 1,
        "y": 15
      },
      "points": 47
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 2,
      "actionNumber": 14,
      "location": {
        "x": 14,
        "y": 10
      },
      "points": 81
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": -3,
      "actionNumber": 15,
      "location": {
        "x": 8,
        "y": 10
      },
      "points": 78
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 5,
      "actionNumber": 11,
      "location": {
        "x": 13,
        "y": 12
      },
      "points": 52
    },
    "user": "u4285"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": -1,
      "actionNumber": 12,
      "location": {
        "x": 2,
        "y": 4
      },
      "points": 51
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": -2,
      "actionNumber": 16,
      "location": {
        "x": 16,
        "y": 11
      },
      "points": 76
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": -2,
      "actionNumber": 17,
      "location": {
        "x": 14,
        "y": 13
      },
      "points": 74
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 4,
      "actionNumber": 18,
      "location": {
        "x": 14,
        "y": 1
      },
      "points": 78
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 4,
      "actionNumber": 13,
      "location": {
        "x": 15,
        "y": 12
      },
      "points": 55
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 10,
      "actionNumber": 19,
      "location": {
        "x": 13,
        "y": 1
      },
      "points": 88
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": 12,
      "actionNumber": 14,
      "location": {
        "x": 10,
        "y": 5
      },
      "points": 67
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 5,
      "actionNumber": 20,
      "location": {
        "x": 16,
        "y": 9
      },
      "points": 93
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "Move",
      "pointsAdded": -2,
      "actionNumber": 15,
      "location": {
        "x": 2,
        "y": 7
      },
      "points": 65
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 8,
      "actionNumber": 21,
      "location": {
        "x": 2,
        "y": 12
      },
      "points": 101
    },
    "user": "u5100"
  },
  {
    "game": 2,
    "action": {
      "actionType": "gameEnd",
      "gameStatus": "Loss",
      "actionNumber": 16,
      "points": 65
    },
    "user": "u4285"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 2,
      "actionNumber": 22,
      "location": {
        "x": 16,
        "y": 6
      },
      "points": 103
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "SpecialMove",
      "pointsAdded": -2,
      "move": "Clear",
      "actionNumber": 23,
      "points": 101
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 1,
      "actionNumber": 24,
      "location": {
        "x": 10,
        "y": 10
      },
      "points": 102
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 5,
      "actionNumber": 25,
      "location": {
        "x": 3,
        "y": 12
      },
      "points": 107
    },
    "user": "u5100"
  },
  {
    "game": 3,
    "action": {
      "actionType": "gameStart",
      "actionNumber": 1
    },
    "user": "u9920"
  },
  {
    "game": 3,
    "action": {
      "actionType": "Move",
      "pointsAdded": 6,
      "actionNumber": 2,
      "location": {
        "x": 8,
        "y": 7
      },
      "points": 6
    },
    "user": "u9920"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": -10,
      "actionNumber": 26,
      "location": {
        "x": 13,
        "y": 8
      },
      "points": 97
    },
    "user": "u5100"
  },
  {
    "game": 3,
    "action": {
      "actionType": "Move",
      "pointsAdded": 11,
      "actionNumber": 3,
      "location": {
        "x": 11,
        "y": 16
      },
      "points": 17
    },
    "user": "u9920"
  },
  {
    "game": 3,
    "action": {
      "actionType": "SpecialMove",
      "pointsAdded": -6,
      "move": "Clear",
      "actionNumber": 4,
      "points": 11
    },
    "user": "u9920"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 1,
      "actionNumber": 27,
      "location": {
        "x": 19,
        "y": 20
      },
      "points": 98
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 12,
      "actionNumber": 28,
      "location": {
        "x": 6,
        "y": 6
      },
      "points": 110
    },
    "user": "u5100"
  },
  {
    "game": 1,
    "action": {
      "actionType": "Move",
      "pointsAdded": 10,
      "actionNumber": 29,
      "location": {
        "x": 16,
        "y": 18
      },
      "points": 120
    },
    "user": "u5100"
  },
  {
    "game": 3,
    "action": {
      "actionType": "Move",
      "pointsAdded": 9,
      "actionNumber": 5,
      "location": {
        "x": 7,
        "y": 7
      },
      "points": 20
    },
    "user": "u9920"
  }
]

db.foo.insert(doc);



cursor = db.foo.find();
while ( cursor.hasNext() ) {
   printjson( cursor.next() );
}



