export const nestedData = {
    schema: {  commit: 'STRING',  author: {   name: 'STRING',   email: 'STRING',   time_sec: 'INTEGER',   tz_offset: 'INTEGER',   date: {    seconds: 'INTEGER',    nanos: {     type: 'INTEGER'    },   },  }, committer: {  name: 'STRING',  email: 'STRING'  }
    }
   }