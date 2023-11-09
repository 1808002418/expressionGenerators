## input1
```json
{
    "expressionEntities": [
        {
            "f1": "User_Type",
            "f2": "TERM",
            "f3": "00"
        },
        {
            "f1": "User_ID",
            "f2": "TERM",
            "f3": "123456"
        }
    ],
    "logic": "MUST"
}
```
## output1
```json
{
  "from": 0,
  "size": 10,
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "User_Type": {
              "value": "00",
              "boost": 1.0
            }
          }
        },
        {
          "term": {
            "User_ID": {
              "value": "123456",
              "boost": 1.0
            }
          }
        },
        {
          "range": {
            "Operate_Time": {
              "from": 1601481600000,
              "to": 1603814400000,
              "include_lower": true,
              "include_upper": true,
              "boost": 1.0
            }
          }
        }
      ],
      "adjust_pure_negative": true,
      "boost": 1.0
    }
  },
  "sort": [
    {
      "Operate_Time": {
        "order": "desc"
      }
    }
  ]
}
```

---

## input2
```json
{
    "logic": "MUST",
    "expressionEntities": [
        {
            "logic": "SHOULD",
            "expressionEntities": [
                {
                    "f1": "User_Type",
                    "f2": "WILDCARD",
                    "f3": "00"
                },
                {
                    "f1": "User_ID",
                    "f2": "TERM",
                    "f3": "350321199612220717"
                }
            ]
        },
        {
            "f1": "User_ID",
            "f2": "TERM",
            "f3": "350321199612220717"
        }
    ]
}
```
## output2
```json
{
  "from": 0,
  "size": 10,
  "query": {
    "bool": {
      "must": [
        {
          "bool": {
            "should": [
              {
                "wildcard": {
                  "User_Type": {
                    "wildcard": "00",
                    "boost": 1.0
                  }
                }
              },
              {
                "term": {
                  "User_ID": {
                    "value": "350321199612220717",
                    "boost": 1.0
                  }
                }
              }
            ],
            "adjust_pure_negative": true,
            "boost": 1.0
          }
        },
        {
          "term": {
            "User_ID": {
              "value": "350321199612220717",
              "boost": 1.0
            }
          }
        },
        {
          "range": {
            "Operate_Time": {
              "from": 1601481600000,
              "to": 1603814400000,
              "include_lower": true,
              "include_upper": true,
              "boost": 1.0
            }
          }
        }
      ],
      "adjust_pure_negative": true,
      "boost": 1.0
    }
  },
  "sort": [
    {
      "Operate_Time": {
        "order": "desc"
      }
    }
  ]
}
```