var express = require('express');  //Express frameork object
var morgan = require('morgan');  //Morgan Framework object
var path = require('path');
var pg=require('pg');
var Transaction=require('pg-transaction');
var Pool=pg.Pool;
var crypto = require('crypto');
var bodyParser = require('body-parser');
var session = require('express-session');
var random=require('unique-random');
var rand=random(1000000,99999999);
var app = express();
app.use(morgan('combined'));
app.use(bodyParser.json());
app.use(session({
    secret: 'someRandomSecretValue',
    cookie: { maxAge: 1000 * 60 * 60 * 24 * 30},
    saveUninitialized:true,
    resave:true
}));
var config = {
    user: 'shopkart',
    database: 'shopkart',
    host: 'localhost',
    port: '5432',
    password: 'shopkart'
};
var pool=new Pool(config);
var Client =pg.Client;

var client = new Client(config);
client.connect();
function TransactionException(message) {
   this.message = message;
   this.name = 'UserException';
}
var die = function(err){
  if (err){console.log(err); throw new TransactionException("Found error! Rolling back.");}
};
/*
var rollback = function(pool) {
  pool.query('ROLLBACK', function() {
   // client.end();
  });
};*/


app.get('/', function (req, res) {
  res.json({msg:"Hello"});
});
app.use(express.static('public'));
app.use('/img',express.static(__dirname+'/img'));

/*Function Definition starts here*/
function msg(msg){
  return JSON.stringify({msg:msg});
}

function hash (input, salt) {
    var hashed = crypto.pbkdf2Sync(input, salt, 10000, 512, 'sha512');
    return ["pbkdf2", "10000", salt, hashed.toString('hex')].join('$');
}

function checkAuth(req, res, next) {
console.log(req.session);
  if (!req.session || !req.session.auth || !req.session.auth.username) {
    res.status(403).send(JSON.stringify({msg:'You must be logged in to proceed.',id:0}));
  } else {
    next();
  }
}
function checkAdmin(req,res,next){
  if(!req.session.auth.username=="blogRootUser"){
    res.status(403).send('I won\'t give you free acess to unauthorized area.');
  }
  else{
    next();
  }

}
function emailValidate(email){
  var atpos = email.indexOf("@");
  var dotpos = email.lastIndexOf(".");
  if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
      return false;
  }
    return true;
}
/*Functions end here*/
/*Table objects json creation functions start here*/
function returnCategories(data){
  var categories=[];
  var category=[];

  for(i=0;i<data.rows.length;i++){
    category={
      id:data.rows[i].id,
      name:data.rows[i].name
    };
    categories.push(category);
  }
  return JSON.stringify(categories);  //JSON object having arrays of names and ids
}

function returnCustomers(data){
  var customers=[];
  var customer={};
  for(i=0;i<data.rows.length;i++){
    customer={
      id:data.rows[i].id,
      name:data.rows[i].name,
      username:data.rows[i].username,
      phone:data.rows[i].phone,
      email:data.rows[i].email,
      address_line_1:data.rows[i].address_line_1,
      address_line_2:data.rows[i].address_line_2,
      landmark:data.rows[i].landmark,
      city:data.rows[i].city,
      state:data.rows[i].state,
      pincode:data.rows[i].pincode
    };
    customers.push(customer);
  }
  return JSON.stringify(customers);
}
function returnRetailers(data){
  var retailers=[];
  var retailer={};
  for(i=0;i<data.rows.length;i++){
    retailer={
      id:data.rows[i].id,
      name:data.rows[i].name,
      username:data.rows[i].username,
      phone:data.rows[i].phone,
      email:data.rows[i].email,
      address_line_1:data.rows[i].address_line_1,
      address_line_2:data.rows[i].address_line_2,
      landmark:data.rows[i].landmark,
      city:data.rows[i].city,
      state:data.rows[i].state,
      pincode:data.rows[i].pincode,
      longitude:data.rows[i].longitude,
      latitude:data.rows[i].latitude
    };
    retailers.push(retailer);
  }
  return JSON.stringify(retailers);
}

function returnProducts(data){

  var product={},products=[];
  for(i=0;i<data.rows.length;i++){
    product={
        id:data.rows[i].id,
        name:data.rows[i].name,
        price:data.rows[i].price,
        description:data.rows[i].description,
        category_id:data.rows[i].category_id,
        category_name:data.rows[i].category_name,
        retailer_id:data.rows[i].retailer_id,
        retailer_name:data.rows[i].retailer_name,
        rating:data.rows[i].rating,
        no_of_rating:data.rows[i].no_of_rating,
        stock:data.rows[i].stock
      };
      products.push(product);
    }
  return JSON.stringify(products);

}

function returnCart(data){
  var cartProduct={},cartProducts=[];
  for(i=0;i<data.rows.length;i++){
    cartProduct={
        id:data.rows[i].id,
        name:data.rows[i].name,
        price:data.rows[i].price,
        category_id:data.rows[i].category_id,
        retailer_id:data.rows[i].retailer_id,
        retailer_name:data.rows[i].retailer_name,
        stock:data.rows[i].stock,
        quantity:data.rows[i].quantity
      };
      cartProducts.push(cartProduct);
    }
  return JSON.stringify(cartProducts);
}

function returnOrders(data){
  var orders=[];
  for(i=0;i<data.rows.length;i++){

    var order={
      id:data.rows[i].id,
      amount:data.rows[i].amount,
      date_created:data.rows[i].date_created,
      customer_id:data.rows[i].customer_id,
      confirmation_number:data.rows[i].confirmation_number,
      shipping_address:data.rows[i].shipping_address,
      card_number:data.rows[i].card_number,
      pincode:data.rows[i].pincode
    };
    orders.push(order);
  }
  return JSON.stringify(orders);
}

function returnOrderedProducts(result){
  var products=[];
    for(j=0;j<result.rows.length;j++){
      var product={
        price:result.rows[j].price,
        productId:result.rows[j].product_id,
        quantity:result.rows[j].quantity,
        rating:result.rows[j].rating,
        status:result.rows[j].status,
        confirmation_number:result.rows[j].confirmation_number
      };
      products.push(product);
    }
    return JSON.stringify(products);
}


/*Table objects json creation functions ends here*/
app.get('/removeFromCart/product/:productId/quantity/:quantity',checkAuth,function(req,res){
  pool.query('SELECT quantity from cart where product_id=$1 AND customer_id=$2',[req.params.productId,req.session.auth.id],function(err,result){
    if(err){
      res.status(500).send(msg("Error removing item."));
    }
    else if(result.rows[0].quantity>req.params.quantity){
      pool.query('UPDATE cart set quantity=quantity-$1 where product_id=$2 AND customer_id=$3',[req.params.quantity,req.params.productId,req.session.auth.id],function(erru,resultu){
        if(erru){
          res.status(500).send(msg("Error Updating Quantity."));
        }
        else{
          res.send(msg("Updated Cart"));
        }
      });
    }else if (result.rows[0].quantity==req.params.quantity){
      pool.query('DELETE from cart where product_id=$1 AND customer_id=$2',[req.params.productId,req.session.auth.id],function(erru,resultu){
        if(erru){
          res.status(500).send(msg("Error Updating Quantity."));
        }
        else{
          res.send(msg("Removed Item"));
        }
      });

    }else{
      res.send(msg("Invalid data"));
    }
  });
});

/*id can be "customer_id" or "confirmation_number".*/
app.get('/myOrders/:id',checkAuth,function(req,res){
  var customerId=req.params.id.split('$')[0];
  var confirmation_number=req.params.id.split('$')[1];
  var args=[];
  var sqlQuery='SELECT customer_order.* from "customer_order"';
  if(customerId && customerId!="all" && customerId==req.session.auth.id){
    sqlQuery+=" WHERE customer_id=$1";
    args.push(customerId);
  }else if(confirmation_number && confirmation_number!="all"){
    sqlQuery+=" WHERE confirmation_number=$1";
    args.push(confirmation_number);
  }else{
    sqlQuery+=" WHERE $1=$1";
  }
  sqlQuery+= ' ORDER BY date_created DESC';
    pool.query(sqlQuery,args,function(err,result){
      if(err){
        res.status(404).send(JSON.stringify({msg:"Error retiriving orders found."+err.toString()}));
      }
      else{
        res.send(returnOrders(result));
      }
    });
});

/*
app.get('/getMyOrders/',checkAuth,function(req,res){
  const orders=[];
  const products=[];
  pool.query('SELECT confirmation_number,date_created,shipping_address,card_number,pincode from customer_order where customer_id=$1',[req.session.auth.id],function(err,result){

    if(err){
      res.status(500).send(msg("error:"+err.toString()));
    }else{
    for(i=0;i<result.rows.length;i++){
      orders.push({
        "confirmation_number":result.rows[i].confirmation_number,
        "date_created":result.rows[i].date_created,
        "shipping_address":result.rows[i].shipping_address,
        "card_number":result.rows[i].card_number,
        "pincode":result.rows[i].pincode,
      });
    }
    console.log(orders);
  }
  });

  console.log(orders[0]);
  var args=[];
  for(j=0;j<orders.length;j++){
    args.push(orders[j].confirmation_number);
    console.log(orders[j].confirmation_number);
  }

    pool.query('SELECT product_id,"product".name,"product".price,"ordered_product".quantity,status from "ordered_product","product" where confirmation_number IN('+args.join(',')+') AND product_id="product".id',function(err,result){

      if(err){
          res.status(500).send(msg("error:"+err.toString()));
      }
      else {

        for(i=0;i<orders.length;i++){
          products.push({
            id:result.rows[i].product_id,
            name:result.rows[i].name,
            quantity:result.rows[i].quantity,
            status:result.rows[i].status,
            price:result.rows[i].price
          });
        }
        orders["cart"]=products;
          res.send(JSON.stringify(orders));
      }

    });


  });
*/
app.get('/getOrderDetails/:id',checkAuth,function(req,res){
  var customerId=req.params.id.split('$')[0];
  var confirmation_number=req.params.id.split('$')[1];
  var args=[];
  var sqlQuery='SELECT "ordered_product".*,"product".name from "ordered_product","product" WHERE "product".id="ordered_product".product_id AND';
  if(customerId && customerId!="all" && customerId==req.session.auth.id){
    sqlQuery+=" customer_id=$1";
    args.push(customerId);
  }else if(confirmation_number && confirmation_number!="all"){
    sqlQuery+=" confirmation_number=$1";
    args.push(confirmation_number);
  }else{
    sqlQuery+=" $1=$1";
    args.push(req.params.id);
  }
  sqlQuery+= ' ORDER BY DESC';
  console.log(sqlQuery);
  pool.query(sqlQuery,args,function(err,result){
    if(err){
      res.status(500).send(msg("Error Retrieving Data from database"+err.toString()));
    }
    else{
      res.send(returnOrderedProducts(result));
    }
  });
});




app.get('/category/:categoryId',function(req,res){
  var sqlQuery='SELECT * from "category"';
  var categoryId=req.params.categoryId;
  if(categoryId!="all"){
    sqlQuery+=" WHERE id=$1";
  }else{
    sqlQuery+=" WHERE $1=$1";
  }
  pool.query(sqlQuery,[categoryId],function(err,result){
    if(err){
      res.status(404).send(JSON.stringify({msg:"Error not found."}));
    }
    else{
      res.send(returnCategories(result));
    }
  });
});
app.get('/retailer/:retailerId',checkAuth,function(req,res){
  var sqlQuery='SELECT * from "retailer"';
  var retailerId=req.params.retailerId;
  if(retailerId!="all"){
    sqlQuery+=" WHERE id=$1";
  }else{
    sqlQuery+=" WHERE $1=$1";
  }
    pool.query(sqlQuery,[retailerId],function(err,result){
      if(err){
        res.status(404).send(JSON.stringify({msg:"Error not found."}));
      }
      else{
        res.send(returnRetailers(result));
      }
    });
});
app.get('/customer/:customerId',checkAuth,function(req,res){
  var sqlQuery='SELECT * from "customer"';
  var customerId=req.params.customerId;
  if(customerId!="all" && customerId==req.session.auth.id){
    sqlQuery+=" WHERE id=$1";
  }else{
    sqlQuery+=" WHERE $1=$1";
  }
    pool.query(sqlQuery,[customerId],function(err,result){
      if(err){
        res.status(404).send(JSON.stringify({msg:"Error not found."}));
      }
      else{
        res.send(returnCustomers(result));
      }
    });
});

app.get('/clearCart',checkAuth,function(req,res){
  pool.query('DELETE from cart where customer_id=$1',[req.session.auth.id],function(err,result){
    if(err){
      res.status(500).send(msg("Error clearing Cart"));
    }else{
      res.send(msg("Items cleared from cart."));
    }
  });
});


app.get('/viewCart/',checkAuth,function(req,res){
  var customerId=req.session.auth.id;
  pool.query('SELECT "product".category_name,"product".id,"cart".quantity,"product".name,price,"retailer".id as retailer_id,"retailer".name as retailer_name,stock from "cart","product","retailer","category" where "product".retailer_id="retailer".id AND "category".id="product".category_id AND "cart".product_id="product".id AND "cart".customer_id=$1 ORDER BY "product".id DESC',[customerId],function(err,result){
    if(err){
      console.log(err);
      res.status(500).send(msg("Error Retrieving Cart"));
    }else{
      res.send(returnProducts(result));
    }
  });
});


app.get('/addToCart/product/:productId/quantity/:quantity',checkAuth,function(req,res){
  var args=[req.params.quantity,req.session.auth.id,req.params.productId];
  pool.query('UPDATE "cart" set quantity=quantity+$1 where customer_id=$2 AND product_id=$3',args,function(err,resUp){
    if(err){
      res.status(500).send(msg("Cart update error"));
    }else{
      if(resUp.rows.length==0){
        pool.query('INSERT INTO "cart"(quantity,customer_id,product_id) values($1,$2,$3)',args,function(erri,result){
          if(err){
            res.status(500).send(msg("Cart insert error"));
          }
          else{
            res.send(msg("Item added to cart."));
          }
        });
      }else{
        res.send(msg("Item added to cart."));
      }
    }
  });

});





function orderProduct(data,customerId,confirmation_number){

	  var isFailure=true;
	  var amount=data.amount;
    var price=data.price;
	  var cart=data.cart;
	  var shipping_address=data.shipping_address;
	  var pincode=data.pincode;
	  var card_number=data.card_number;
	 // var quantity=data.quantity;
	  var ctr=0;


	do{
		var tx = new Transaction(client);
		tx.on('error', die);

		try{
		tx.begin();
		tx.savepoint('savepoint1');
		tx.query('INSERT INTO "customer_order" ("amount", "date_created", "customer_id", "confirmation_number", "shipping_address", "card_number","pincode") VALUES ($1,$2,$3,$4,$5,$6,$7)', [amount,new Date(),customerId,confirmation_number,shipping_address,card_number,pincode]);
		for(i=0;i<cart.length;i++){
		  var productId=cart[i].id;
		  var quantity=cart[i].quantity;
			tx.query('UPDATE "product" SET "stock"=stock-$1 WHERE id= $2', [quantity,productId]);
		}
		for(i=0;i<cart.length;i++){

			  var productId=cart[i].id;
			  var price=cart[i].price;
			  var quantity=cart[i].quantity;
			tx.query('INSERT INTO "ordered_product" ("confirmation_number","product_id","quantity","price") VALUES ($1,$2,$3,$4)', [confirmation_number,productId,quantity,price]);
		}
		tx.release('savepoint1'); // can no longer use savepoint1 as a point to rollback to
		tx.commit();
		isFailure=false;
		}
		catch(e){
				isFailure=true;
				tx.rollback('savepoint1');
				tx.commit;
				console.log(e);
		}
		ctr++;

	}while(isFailure && ctr<5);
	return isFailure;

}
app.post('/orderProduct/',checkAuth,function(req,res){
  var confirmation_number=rand();
	var failure=orderProduct(req.body,req.session.auth.id,confirmation_number);
	if(failure) res.status(500).send(msg("Sorry"));
	else res.json({confirmation_number:confirmation_number,msg:"Order successfully placed."});
});
/*app.post('/orderProduct/:customerId',checkAuth,function(req,res){

  var success=false;
  var customerId=req.params.customerId;
  var amount=req.body.amount;
  var cart=req.body.cart;
  var shipping_address=req.body.shipping_address;
  var pincode=req.body.pincode;
  var card_number=req.body.card_number;

  var confirmation_number=rand();
  console.log("Order Id:"+confirmation_number+"\n");


  pool.query('BEGIN', function(err, result) {
    if(err){
		console.log(err.toString());
		rollback(pool);
		res.status(500).send(msg("1Sorry! This transaction couldn't be processed."+err.toString()));
	}else{

		pool.query('INSERT INTO "customer_order" ("amount", "date_created", "customer_id", "confirmation_number", "shipping_address", "card_number","pincode") VALUES ($1,$2,$3,$4,$5,$6,$7)', [amount,new Date(),customerId,confirmation_number,shipping_address,card_number,pincode], function(err, result) {
		  if(err){
				console.log(err.toString());
				rollback(pool);
				res.send(msg("2Sorry! This transaction couldn't be processed."+err.toString()));
			}else{
					for(i=0;i<cart.length;i++){
						console.log(i);
					  var productId=cart[i].product_id;
					  var price=cart[i].price;
					  var quantity=cart[i].quantity;

					  pool.query('UPDATE "product" SET "stock"=stock-$1 WHERE id= $2', [quantity,productId], function(err, result) {
								if(err){
									console.log(err.toString());
									rollback(pool);
									res.send(msg("3Sorry! This transaction couldn't be processed."+err.toString()));
								}else{
										pool.query('INSERT INTO "ordered_product" ("confirmation_number","product_id","quantity") VALUES ($1,$2,$3)', [confirmation_number,productId,quantity], function(err, result) {
											  if(err){
													console.log(err.toString());
													rollback(pool);
													res.send(msg("4Sorry! This transaction couldn't be processed."+err.toString()));
												}else{
														console.log("Product added.");

												}

										});

								}

					  });

					}

					pool.query('COMMIT', function(err,result){
						if(err){
							console.log(err.toString());
							rollback(pool);
							res.status(500).send(msg("lastSorry! This transaction couldn't be processed."+err.toString()));
						}
						else{
							res.send(JSON.stringify({confirmation_number:confirmation_number,msg:"Thank you for shopping with us."}));
						}
					},pool.end.bind(pool));
			}
			res.status(500).json({msg:"Sorry"});
		});

	}
	/*success=true;
   });/*
  if(success){
    res.send(JSON.stringify({confirmation_number:confirmation_number,msg:"Thank you for shopping with us."}));
  }
  else {
    res.status(500).send(msg("Sorry! This transaction couldn't be processed."));
  }
});*/

/*id can be "category_id" or "product_id".*/
app.get('/product/:id',function(req,res){
  var category_id=req.params.id.split('$')[0];
  var product_id=req.params.id.split('$')[1]
  console.log("Category:"+category_id+",Product:"+product_id);
  var selectionArgs=[];
  var sqlQuery='SELECT "product".id,"product".name,price,description,category_id,"category".name as category_name,"retailer".id as retailer_id,"retailer".name as retailer_name,rating,no_of_rating,stock from "product","retailer","category" where "product".retailer_id="retailer".id AND "category".id="product".category_id';
  if(category_id && category_id!="all"){
    sqlQuery+=' AND category_id=$1';
    selectionArgs.push(category_id);
  }else if(product_id && product_id!="all"){
    sqlQuery+=' AND "product".id=$1';
    selectionArgs.push(product_id);
  }
  else {
    sqlQuery+=' AND $1=$1 ORDER BY id DESC';
    selectionArgs.push(category_id);
  }



  pool.query(sqlQuery,selectionArgs,function(err,result){
    if(err){
      console.log(err);
      res.status(404).send(msg("Error not found."+err.toString()));
    }
    else{
      res.send(returnProducts(result));
    }
  });
});

app.post('/changePassword',checkAuth,function(req,res){

	var currentPassword=req.body.currentPassword;
	var password=req.body.password;
      pool.query('SELECT password FROM "customer" WHERE id=$1', [req.session.auth.id], function (err, result) {
         if (err) {
             console.log(err.toString());
             res.status(500).send(msg(err.toString()));
         } else {
             if (result.rows.length === 0) {
               console.log("Invalid");
                 res.status(403).send(msg('Current User is invalid'));
             } else {
                 // Match the password
                 var dbString = result.rows[0].password;
                 var salt = dbString.split('$')[2];
                 var hashedPassword = hash(currentPassword, salt); // Creating a hash based on the password submitted and the original salt
                 if (hashedPassword === dbString) {
                     pool.query('UPDATE customer set password=$1 where id=$2',[hash(password,crypto.randomBytes(128).toString('hex')),req.session.auth.id],function(err,result){
                         if(err) res.status(500).send(msg("Error occurred "+err.toString));
                         else{
                           res.send(msg("Password Updated Successfully."));
                         }
                       });
                 } else {
                   console.log("Wrong creds");
                   res.status(403).send(msg("Invalid current password."));
                 }
             }
         }
      });

});

app.post('/register/:userType/updateBool/:bool/',function(req,res){
  var address=req.params.address;
  var updateBool=req.params.bool;
  var username=req.body.username;
  var name=req.body.name;
  var password=req.body.password;
  var email=req.body.email;
  var phone=req.body.phone;
  var address_line_1=req.body.address_line_1;
  var address_line_2=req.body.address_line_2;
  var city=req.body.city;
  var state=req.body.state;
  var landmark=req.body.landmark;
  var pincode=req.body.pincode;

  console.log(username+":"+email+":"+phone+":"+(new Date()));
  if(updateBool && updateBool=="true"){

      pool.query('UPDATE customer set address_line_1=$1,address_line_2=$2,city=$3,state=$4,landmark=$5,pincode=$6 where id=$7',[address_line_1,address_line_2,city,state,landmark,pincode,req.session.auth.id],function(err,result){
        if(err){
          res.status(500).send(msg("Error occurred "+err.toString));
        }else{
          res.send(msg("Address Updated Successfully."));
        }
      });

    }
  else{
      if(req.params.userType=="customer"){
          if(!emailValidate(email)){
              res.status(400).send('Should I tell you the format of email?');
          }else{
          var salt = crypto.randomBytes(128).toString('hex');
          password = hash(password, salt);
          pool.query('SELECT username from "customer" where username=$1 OR email=$2 OR phone=$3',[username,email,phone],function(err,result){
            if(err){
              console.log(err.toString());
              res.status(500).send(mag('Something bad happened on our side.'+err.toString()));
            }
            else{
               if(result.rows.length===0){
                 pool.query('INSERT INTO "customer"(username,password,email,phone,address_line_1,address_line_2,state,city,landmark,pincode,name) values($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11)',[username,password,email,phone,address_line_1,address_line_2,state,city,landmark,pincode,name],function(errc,resultc){
                   if(errc){
                     console.log(errc.toString());
                     res.status(500).send(msg('Something Unexpected happened. Please Try Again Later.')+errc.toString());
                   }
                   else{
                     res.send(msg('User successfully registered. You can login now.'));
                   }
                 });
               }
               else{
                 res.status(403).send(msg('Another Account Already Exists With This Username or Email or Mobile already registered.'));
               }
             }
          });
        }
      }else if(req.params.userType==="retailer") {
        var latitude=req.params.latitude;
        var longitude=req.params.longitude;
        if(!emailValidate(email)){
            res.status(400).send('Should I tell you the format of email?');
        }else{
        var salt = crypto.randomBytes(128).toString('hex');
        password = hash(password, salt);
        pool.query('SELECT username from "retailer" where username=$1 OR email=$2 OR phone=$3',[username,email,phone],function(err,result){
          if(err){
            console.log(err.toString());
            res.status(500).send(mag('Something bad happened on our side.'+err.toString()));
          }
          else{
             if(result.rows.length===0){
               pool.query('INSERT INTO "customer"(username,password,email,phone,address_line_1,address_line_2,state,city,landmark,pincode,name,longitude,latitude) values($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13)',[username,password,email,phone,address_line_1,address_line_2,state,city,landmark,pincode,name,longitude,latitude],function(errc,resultc){
                 if(errc){
                   console.log(errc.toString());
                   res.status(500).send(msg('Something Unexpected happened. Please Try Again Later.')+errc.toString());
                 }
                 else{
                   res.send(msg('User successfully registered. You can login now.'));
                 }
               });
             }
             else{
               res.status(403).send(msg('Another Account Already Exists With This Username or Email or Mobile already registered.'));
             }
           }
        });
      }
    }
  }
});

app.post('/login',function(req,res){

  var username = req.body.username;
  var password = req.body.password;
  console.log(username+":"+password);
   pool.query('SELECT id,username,password FROM "customer" WHERE username=$1 OR email=$1 OR phone=$1', [username], function (err, result) {
      if (err) {
          console.log(err.toString());
          res.status(500).send(msg(err.toString()));
      } else {
          if (result.rows.length === 0) {
            console.log("Invalid");
              res.status(403).send(msg('Username or Password is invalid'));
          } else {
              // Match the password
              var dbString = result.rows[0].password;
              var salt = dbString.split('$')[2];
              var hashedPassword = hash(password, salt); // Creating a hash based on the password submitted and the original salt
              if (hashedPassword === dbString) {

                // Set the session
                req.session.auth = {username: result.rows[0].username,id:result.rows[0].id};
                // set cookie with a session id
                // internally, on the server side, it maps the session id to an object
                // { auth: {userId }}

                res.send(JSON.stringify({msg:'You have entered the correct credentials.',id:result.rows[0].id}));

              } else {
                console.log("Wrong creds");
                res.status(403).send(msg("Don't give me wrong credentials."));
              }
          }
      }
   });
});
app.get('/check-login',checkAuth,function (req, res) {
  console.log(req.session+"\n");
  var username=req.session.auth.username;
  var id=req.session.auth.id;
    res.send(JSON.stringify({msg:'You are logged in',username:username,id:id}));
});
app.get('/logout', checkAuth,function (req, res) {
   delete req.session.auth;
   res.send(msg('You logged out successfully. Visit again!'));
});
/*Post Request End Here*/


var port = 8080;// Use 8080 for local development because you might already have apache running on 80
app.listen(port, function () {
  console.log(`Shopkart app listening on port ${port}!`);
});
