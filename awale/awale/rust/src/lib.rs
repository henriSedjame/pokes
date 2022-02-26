mod utils;

use wasm_bindgen::prelude::*;


// When the `wee_alloc` feature is enabled, use `wee_alloc` as the global
// allocator.
#[cfg(feature = "wee_alloc")]
#[global_allocator]
static ALLOC: wee_alloc::WeeAlloc = wee_alloc::WeeAlloc::INIT;

#[wasm_bindgen(module = "src/app/shared.ts")]
extern "C" {

  /* Player */
  pub type Player;

  #[wasm_bindgen(constructor)]
  fn new_player(player_type:usize) -> Player;

  #[wasm_bindgen(method, getter, js_name = get_points)]
  fn points(this: &Player) -> u8;

  #[wasm_bindgen(method, setter, js_name = set_points)]
  fn set_points(this: &Player, value: u8);

  #[wasm_bindgen(method, setter, js_name = set_turn)]
  fn set_turn(this: &Player, value: bool);

  #[wasm_bindgen(method, getter, js_name = get_turn)]
  fn is_turn(this: &Player) -> bool ;

  /* Game */
  pub type Game;

  #[wasm_bindgen(constructor)]
  fn new(p1: Player, p2: Player, holes: Vec<u8>) -> Game;

  #[wasm_bindgen(method, getter, js_name = get_holes)]
  fn holes(this: &Game) -> Vec<u8>;

  #[wasm_bindgen(method, getter, js_name = get_p1)]
  fn p1(this: &Game) -> Player;

  #[wasm_bindgen(method, getter, js_name = get_p2)]
  fn p2(this: &Game) -> Player;

  /* Game channel*/
  pub type GameChannel;

  #[wasm_bindgen(method,  js_name = send)]
  fn send(this: &GameChannel, evt: GameEvent);

  /* GameEvent*/
  pub type GameEvent;

  #[wasm_bindgen(constructor)]
  fn new_event(running: bool, index: usize, nb: u8, count: u8, player: bool, gain: Option<u8> ) -> GameEvent;


}

#[wasm_bindgen]
extern {
  #[wasm_bindgen(js_namespace = console)]
  fn log(s: &str);
}

#[wasm_bindgen]
pub fn start(user_first: bool) -> Game {
  let player = Player::new_player(0);
  let pc = Player::new_player(1);

  if user_first {
    player.set_turn(true);
  } else {
    pc.set_turn(true);
  }

  Game::new(player, pc, vec![4; 12] )
}

#[wasm_bindgen]
pub fn run(game: &Game, i: usize) -> Game {
  log(format!("Run {:?}", i).as_str());

  let result = next(i, game.holes());

  let p1 = game.p1();

  let p2 = game.p2();

  if i > 5 {
    p2.set_points(p2.points() + result.0);
  } else {
    p1.set_points(p1.points() + result.0);
  }

  p2.set_turn(!p2.is_turn());
  p1.set_turn(!p1.is_turn());

  Game::new(p1, p2, result.1)
}

#[wasm_bindgen]
pub struct NextResult(u8, Vec<u8>);

#[wasm_bindgen]
pub fn next(i: usize, state: Vec<u8>) ->  NextResult {

  if state[i] == 0 {
    return NextResult(0, state)
  }

  let mut v: Vec<u8> = Vec::from(state);

  log(format!("current state : {:?} With index {}", v, i).as_str());

  let is_user_row = |i: usize| i <= 5;

  // le nombre de graines dans le trou de départ
  let mut count = v[i];

  //log(format!("count : {:?}", count).as_str());

  // l'index du trou courant
  let mut current = i ;


  // on vide le trou de départ
  v[i] = 0;


  while count > 0 {

    current = (current + 1) % 12;

    //log(format!("current index : {:?}", current).as_str());

    // Si le nombre de graines prises dans un trou excède 11,
    // on sème pendant un tour complet, on saute le trou de départ,
    // puis on continue à semer dans les autres trous suivants.
    if current == i {
      //log(format!("Continue !!!! ").as_str());
      continue;
    }

    v[current] += 1;

    count -= 1;

    //log(format!("count : {:?}", count).as_str());
  }

  if (is_user_row(i) == is_user_row(current)) && v[current] != 1 {

    //log(format!("Same line").as_str());

    let nr = next(current, v);

    return nr;

  } else  {

    let mut p = 0;

    while current >= (if i > 5 { 0 } else { 6 }) {
      log(format!("Calculate Points current : {}", current).as_str());

      let pt = v[current];

      if pt !=2 && pt != 3 {

        break;

      } else {

        p += pt;
        v[current] = 0;
        if current == 0 {
          break;
        } else {
          current -= 1;
        }

        log(format!("Points : {}", p).as_str());

      }

    }

    return NextResult(p, v);

  }

}

#[wasm_bindgen]
pub fn pc_choice(state: Vec<u8>)  -> Vec<usize> {

  let mut index: usize = 6;
  let mut points = 0;
  let mut indexes: Vec<usize> = vec![];

  for i in 6..12 {

    log(format!("PC-CHOICE ===> {}", i).as_str());
    if state[i] != 0 {

      indexes.push(i);

      let result = next(i, state.clone());

      if result.0 > points {
        index = i;
        points = result.0;
      }

    }

  }

  return if points != 0  || indexes.len() == 0{
    vec![index]
  } else {
    indexes
  }

}


#[wasm_bindgen]
pub fn play(i: usize, state: Vec<u8>, channel: GameChannel) {

  let mut v: Vec<u8> = Vec::from(state);

  log(format!("current state : {:?} With index {}", v, i).as_str());

  let is_user_row = |i: usize| i <= 5;

  // le nombre de graines dans le trou de départ
  let mut count = v[i];


  let mut current = i ;


  v[i] = 0;

  channel.send(GameEvent::new_event(true, current, v[current], count, is_user_row(i), None));

  while count > 0 {

    current = (current + 1) % 12;

    //log(format!("current index : {:?}", current).as_str());

    // Si le nombre de graines prises dans un trou excède 11,
    // on sème pendant un tour complet, on saute le trou de départ,
    // puis on continue à semer dans les autres trous suivants.
    if current == i {
      continue;
    }

    v[current] += 1;

    count -= 1;

    channel.send(GameEvent::new_event(true, current, v[current], count, is_user_row(i), None));

    log(format!("Current ::: {}", current).as_str());
  }

  log(format!("{} ::: IS USER ROW {} vs {}", current , is_user_row(i), is_user_row(current)).as_str());


  if (is_user_row(i) == is_user_row(current)) && v[current] != 1 {

    log(format!("Run again {}", current).as_str());

     play(current, v, channel);

  } else  {

    let mut p = 0;

    while current > if i > 5 { 0 } else { 6 } {

      let pt = v[current];

      if pt !=2 && pt != 3 {

        break;

      } else {

        p += pt;
        v[current] = 0;

        channel.send(GameEvent::new_event(true, current, v[current], count, is_user_row(i), None));

        current -= 1;

        log(format!("Points : {}", p).as_str());

      }

    }

    channel.send(GameEvent::new_event(false, current, 0, 0, is_user_row(i), Some(p)));

  }

}




